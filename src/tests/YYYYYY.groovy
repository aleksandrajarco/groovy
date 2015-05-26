package tests;

import scripts.StringUtil
class JavaTest2 extends GroovyTestCase {
	def stringUtil = new StringUtil()
	void testConcatenation() {
		def result = stringUtil.concat(['Luke', 'John'], '-')
		assertToString('Luke-John', result)
	}
	void testConcatenationWithEmptyList() {
		def result = stringUtil.concat([], ',')
		assertEquals('', result)
	}
	void testConcatenationWithNullShouldReturnNull() {
		def result = stringUtil.concat(null, ',')
		assertEquals('', result)
		}
}