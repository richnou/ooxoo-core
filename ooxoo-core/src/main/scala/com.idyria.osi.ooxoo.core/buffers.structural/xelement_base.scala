/**
 *
 */
package com.idyria.osi.ooxoo.core.buffers.structural

import scala.annotation.Annotation
import scala.annotation.StaticAnnotation
import scala.reflect.runtime.universe._
import com.idyria.osi.ooxoo.core.utils.ScalaReflectUtils

import java.lang.reflect._

class xelement_base(var name: String = null,var ns: String = null)  {


  //def name() : String = name

  //def ns() : String = namespace

}

object xelement_base {




  def apply(source: AnyRef) : xelement_base = {

    // Get Annotation and instanciate base object
    //--------------------
    var annot = source.getClass.getAnnotation(classOf[xelement])
    if (annot!=null) {

      // Name: annotation content, or Object name
      //---------
      var name = annot.name() match {
        case aname if (aname.length==0) =>

          // Class Name, name can have some funny $annon$...$1 constructs
          // So we need to filter
          var regexp = """(?:.*\$)?([a-zA-Z][\w]+)(?:\$[0-9]+)?\z""".r
          regexp.findFirstMatchIn(source.getClass.getSimpleName) match {
            case Some(m) => m.group(1)
            case None    =>
              //println("Could not match in class name filter")
              source.getClass.getSimpleName
          }

        case aname => aname
      }

      // NS: annotation content
      //---------

      return new xelement_base(name=name,ns=annot.ns())
    }

    return null

  }

   def apply(source: Field) : xelement_base = {

    // Get Annotation and instanciate base object
    //--------------------
    var annot = source.getAnnotation(classOf[xelement])
    if (annot!=null) {

      // Name: annotation content, or Object name
      //---------
      var name = annot.name() match {
        case aname if (aname.length==0) => source.getName
        case aname => aname
      }

      // NS: annotation content
      //---------

      return new xelement_base(name=name,ns=annot.ns())
    }

    return null

  }


}