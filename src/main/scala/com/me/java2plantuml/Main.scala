package com.me.java2plantuml

import scalax.file.Path
import scalax.io.StandardOpenOption._
import java.io.File
import scala.io.Source

object Main extends App {

  case class Config(baseDirectory: String = "", output: String = "", include: String = "*.java")

  val parser = new scopt.immutable.OptionParser[Config]() {
    def options = Seq(
      opt("b", "base-directory", "Base directory to parse") {
        (v, c) => c.copy(baseDirectory = v)
      },
      opt("o", "output", "Name of the file being generated") {
        (v, c) => c.copy(output = v)
      },
      opt("i", "include", "Specify a pattern of files to be included") {
        (v, c) => c.copy(include = v)
      }
    )
  }

  parser.parse(args, Config()) map {
    config =>
      require(!config.baseDirectory.isEmpty, "Base directory shouldn't be empty")
      require(!config.output.isEmpty, "Output shouldn't be empty")
      val baseDir = new File(config.baseDirectory)
      require(baseDir.exists, "Base directory doesn't exist")
      require(baseDir.isDirectory, "Base directory must be a directory")

      def getFileTree(f: File): Stream[File] = {
        lazy val files =
          if (f.isDirectory) f.listFiles().toStream.flatMap(getFileTree)
          else Stream.empty

        f #:: files
      }
      val filter = new FileFilter(config.include)

      val classes = for {
        f <- getFileTree(baseDir).par
        if filter.isMatching(f.getName) && f.getName.endsWith(".java")
      } yield {
        println(s"Parsing java file ${f.getAbsoluteFile}")
        val result = new JavaParser().parse(Source.fromFile(f).mkString)
        if (result.className.isEmpty)
          println(s"Error parsing file ${f.getAbsoluteFile}")
        result
      }

      println("Generate UML diagram")
      val uml = new PlantUmlWriter().buildPlantUml(Seq() ++ classes)
      println(s"Saving to ${config.output}")
      Path.fromString(config.output).outputStream(WriteTruncate: _*).write(uml)
  }
}