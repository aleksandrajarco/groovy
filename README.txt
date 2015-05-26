I. Download zip file and extract it
II.Project in Eclipse
	1.Create groovy project in Eclipse (New->Project->Groovy Project) 
	2.Uncheck option (use default location) and type location of your download project


III. MySQL database
	1. Install mysql ("sudo apt-get install mysql")
	2. Create user "root" with empty password
	3. Connect with database as root (mysql -uroot)
	4. Create database called 'userInfo' ("Create database userInfo")
	5. Move to userInfo database ("use userInfo")
	6. Run following command "Source pathToMysqlBackupFile/mysql_backup.sql"

IV. Running Script:
	1.Script is called 'Application.groovy' and is stored in groovy/src/scripts
	2.Run script with one of following parameters:
		-i insert data from file
		-u update data from file
		-d delete data from file
