package com.me.java2plantuml

/**
 *
 */
class FileFilter(pattern: String) {
  private val matchPattern = pattern
    .replaceAll( """\.""", """\\.""")
    .replaceAll( """\*""", ".*")
    .r

  def isMatching(fileName: String) = {
    matchPattern.findFirstIn(fileName).isDefined
  }
}
