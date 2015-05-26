package tests
import scripts.Application
class JavaTest extends GroovyTestCase {
	def Application = new Application()
	void fileIsNotEmpty(){
		def result=Application.file.length()
		assert(results>0)	
	}
}