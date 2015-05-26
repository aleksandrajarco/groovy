package scripts;
import java.sql.ClientInfoStatus;
import java.util.logging.Logger;

import javax.swing.*
import javax.swing.table.*
import javax.swing.text.DefaultEditorKit.InsertTabAction;

import groovy.sql.*
import groovy.swing.SwingBuilder

import javax.swing.filechooser.FileFilter

//import com.mysql.jdbc.*
import groovy.util.logging.*
//import java.util.logging.Logger
//import groovy.util.logging.Slf4j
//import java.util.logging.Logger

@Grab(group='mysql', module='mysql-connector-java', version='5.1.6')
		@GrabConfig( systemClassLoader=true )

		cl = new CliBuilder(usage: 'groovy clitest -d "dir" [-h] [-n "number"] [arguments]*')
cl.with {
	h(longOpt:'help', 'Show usage information and quit')
	i(argName:'i', longOpt:'insert', type:Boolean, required:false, 'insert data to database')
	d(argName:'d', longOpt:'delete', type:Boolean, required:false, 'delete data from database')
	u(argName:'u', longOpt:'update', type:Boolean, required:false, 'update data in database')
}

def opt = cl.parse(args)



public class Application{
	Application(Integer insert, Integer update, Integer delete, File file){
		this.insert=insert
		this.update=update
		this.delete=delete
		this.file=file
	}

	Integer insert
	Integer update
	Integer delete
	File file

	//static file=new File("/home/ola/Desktop/groovyApp/data.txt")

	def swing = new SwingBuilder()

	def checkOptions(opt){
		Logger logger = Logger.getLogger("")
		if (opt.i==false && opt.d==false && opt.u==false){
			logger.info("no option selected! Exiting application")
		}
	}
	

	def wholeText=file.text

	def sql = Sql.newInstance('jdbc:mysql://localhost:3306/userInfo', 'root', '', 'com.mysql.jdbc.Driver')
	//sql.eachRow('show tables'){ row ->
	//	println 'yyy'+ row[0]
	//}

	def selectDataFromTable(userVal){
		def query="SELECT * from Users WHERE User=$userVal"
		def yyy=sql.firstRow(query)
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
		def date2=date.split('/')
		def day=date2[0].replace(' ','')
		def month=date2[1]
		def year=date2[2]
		def parsed=year+"-"+month+"-"+day
		return parsed
	}

	def parseTemplateFile(){
		wholeText.split('\n\n').each(){ givenUser ->
			def dataList=givenUser.split('\n')
			def user=dataList.find{it.contains('User')}.split('=')[1]
			def dob=parseDateFormat(dataList.find{it.contains('Dob')}.split('=')[1])
			def city=dataList.find{it.contains('City')}.split('=')[1]
			def group=dataList.find{it.contains('Group')}.split('=')[1]

			if (this.insert) insertToTable(user, dob, city, group)
			else if (this.update) updateTable(user, dob, city, group)
			else if (this.delete) deleteFromTable(user)

		}


	}

}


if (opt.i){i=1}else{i=0}
if(opt.u){u=1}else{u=0}
if (opt.d){d=1}else{d=0}

def chooser = new JFileChooser()
chooser.setDialogTitle('Select file')
if (chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
	file = chooser.selectedFile.canonicalPath
	file=new File(file)
} else {
	return
}

app=new Application(i,u,d,file)
app.checkOptions(opt)
app.parseTemplateFile()



