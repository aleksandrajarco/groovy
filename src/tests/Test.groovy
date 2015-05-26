package tests
import scripts.Application
class JavaTest extends GroovyTestCase {
	def Application = new Application()
	void fileIsNotEmpty(){
		def result=Application.file.length()
		assert(result>0)	
	}
	
	void fileContainsDoubleNewLinCharacter(){
		def result=Application.file.read()
		assert(result.contains('\n\n'))
	}
}