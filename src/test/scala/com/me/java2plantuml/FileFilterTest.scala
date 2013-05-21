package com.me.java2plantuml

import org.scalatest.FunSpec
import org.scalatest.matchers.MustMatchers

/**
 *
 */
class FileFilterTest extends FunSpec with MustMatchers {
  describe("The file filter works if") {
    it("Matches the default filter") {
      assert(new FileFilter("*.java").isMatching("MyTest.java") === true)
      assert(new FileFilter("*.java").isMatching("MyTest.scala") === false)
    }

    it("Matches a custom filter") {
      assert(new FileFilter("MyTest.java").isMatching("MyTest.java") === true)
      assert(new FileFilter("MyTest.java").isMatching("MyTest2.scala") === false)
    }
  }
}
