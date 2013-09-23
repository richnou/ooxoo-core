package com.idyria.osi.ooxoo.model.writers


import com.idyria.osi.ooxoo.model._

import java.io.PrintStream

class PrintStreamWriter( var out : PrintStream ) extends Writer {

    var filesWritten = List[String]()

    /**
        Also saves the path of written file for the fileWritten
    */
    def file(path: String) = {

        out.println(s"---------- File: $path -----------------")

        filesWritten =  path :: filesWritten
    }

    def fileWritten(path:String): Boolean = {

        this.filesWritten.contains(path)

    }

    def <<(str: String) : Writer = {
        out.println(s"${this.indentString}$str")
        this
    }

    def finish = {
        
    }

}

/**
    Simply outputs result to stdout, for example for debugging purpose
*/
class StdoutWriter extends PrintStreamWriter (System.out) {
    
    
}
