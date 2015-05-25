import java.sql.ClientInfoStatus;
import java.util.logging.Logger;

import javax.swing.*
import javax.swing.table.*
import javax.swing.text.DefaultEditorKit.InsertTabAction;

import groovy.sql.*
import groovy.swing.SwingBuilder

import javax.swing.filechooser.FileFilter

import groovy.sql.*

//import com.mysql.jdbc.*
import groovy.util.logging.*
//import java.util.logging.Logger
//import groovy.util.logging.Slf4j
//import java.util.logging.Logger

@Grab(group='mysql', module='mysql-connector-java', version='5.1.6')
		@GrabConfig( systemClassLoader=true )

		

def cl = new CliBuilder(usage: 'groovy clitest -d "dir" [-h] [-n "number"] [arguments]*')
cl.h(longOpt:'help', 'Show usage information and quit')
cl.i(argName:'i', longOpt:'insert', type:Boolean, required:false, 'inset data to database')
cl.d(argName:'d', longOpt:'delete', type:Boolean, required:false, 'delete data from database')
cl.u(argName:'u', longOpt:'update', type:Boolean, required:false, 'update data in database')

opt = cl.parse(args)

def checkOptions(opt){
	Logger logger = Logger.getLogger("")
	if (opt.i==false && opt.d==false && opt.u==false){
		logger.info("no option selected! Exiting application")
	}
}

file2=new File("/home/ola/Desktop/groovyApp/data.txt")

swing = new SwingBuilder()

//def chooser = new JFileChooser()
//chooser.setDialogTitle('Select file')
//if (chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
//	file2 = chooser.selectedFile.canonicalPath
//	file2=new File(file2)
//} else {
//	return
//}
wholeText=file2.text

sql = Sql.newInstance('jdbc:mysql://localhost:3306/userInfo', 'root', '', 'com.mysql.jdbc.Driver')
//sql.eachRow('show tables'){ row ->
//	println 'yyy'+ row[0]
//}

def selectDataFromTable(userVal){
	query="SELECT * from Users WHERE User=$userVal"
	yyy=sql.firstRow(query)
	return yyy
}

def insertToTable(userVal,dobVal,cityVal,groupVal){
	if (selectDataFromTable(userVal)==null){
		println "Inserting values to table for user $userVal ..."
		sql.execute("INSERT INTO Users (User,Dob,City,UserGroup) VALUES (?,?,?,?)",
				[
					userVal,
					dobVal,
					cityVal,
					groupVal]
				)
	}
	else{
		Logger logger = Logger.getLogger("")
		logger.warning("User $userVal will not be inserted because already exists..")
	}
}

def deleteFromTable(userVal){

	if (selectDataFromTable(userVal)==null){
		Logger logger = Logger.getLogger("")
		logger.warning("User $userVal will not be deleted becauses does not exist..")
		//println "User $userVal will not be deleted becauses does not exist..."
	}

	else{
		println "Deleteing values from table for user $userVal ..."
		sql.execute("DELETE FROM Users WHERE User =?",
				[userVal])

	}


}

def updateTable(userVal,dobVal,cityVal,groupVal){

	if (selectDataFromTable(userVal)==null){
		Logger logger = Logger.getLogger("")
		logger.warning("User $userVal does not exist in database so the row will not be updated.\nPlease run script with -i [insert] option...")
		//println "User $userVal does not exist in database so the row will not be updated.\nPlease run script with -i [insert] option..."
	}
	else{

		if (selectDataFromTable(userVal)["Dob"].toString()==dobVal
		&& selectDataFromTable(userVal)["City"]==cityVal
		&& selectDataFromTable(userVal)["UserGroup"]==groupVal){
			Logger logger = Logger.getLogger("")
			logger.warning("Nothing to update for user $userVal...")
			//println "Nothing to update for user $userVal..."
		}
		else{
			println "Updating values for user $userVal ..."
			sql.execute("UPDATE Users SET Dob=?, City=?, UserGroup=? WHERE User =?",
					[
						dobVal,
						cityVal,
						groupVal,
						userVal]
					)
		}
	}
}


def parseDateFormat(date){
	xxx=date.split('/')
	day=xxx[0].replace(' ','')
	month=xxx[1]
	year=xxx[2]
	parsed=year+"-"+month+"-"+day
	return parsed
}

def parseTemplateFile(){
	wholeText.split('\n\n').each(){ givenUser ->
		dataList=givenUser.split('\n')
		user=dataList.find{it.contains('User')}.split('=')[1]
		dob=parseDateFormat(dataList.find{it.contains('Dob')}.split('=')[1])
		city=dataList.find{it.contains('City')}.split('=')[1]
		group=dataList.find{it.contains('Group')}.split('=')[1]

		if (opt.i) insertToTable(user, dob, city, group)
		else if (opt.u) updateTable(user, dob, city, group)
		else if (opt.d) deleteFromTable(user)

	}


}

checkOptions(opt)
parseTemplateFile()


