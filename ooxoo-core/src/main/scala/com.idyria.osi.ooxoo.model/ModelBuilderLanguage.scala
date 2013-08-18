package com.idyria.osi.ooxoo.model


import com.idyria.osi.ooxoo.core.buffers.datatypes._
import com.idyria.osi.ooxoo.core.buffers.structural._
import scala.language.implicitConversions

import com.idyria.osi.tea.listeners.ListeningSupport


/**
    This trait contains all the language Wrappers and Conversions used in Model Builder.
    It is separated to lighten real model management in ModelBuilder from convienience language
*/
trait ModelBuilderLanguage extends ListeningSupport {

    // Typing
    //------------------------------

    /**
        Types map is used to map type strings to actual Buffer implementation
    */
    var typesMap = Map[String,Class[_ <: Buffer]](
        ("string" ->  classOf[XSDStringBuffer]),
        ("int" ->  classOf[IntegerBuffer]),
        ("long" ->  classOf[LongBuffer]),
        ("float" ->  classOf[FloatBuffer])
    )

    def getType(str: String) : Class[_ <: Buffer] = {

        this.typesMap.get(str) match {
            case Some(resClass) => resClass
            case None => throw new RuntimeException(s"Specificed type $str did not map to a registered Class")
        }
    }

    //-- Init
  

    // Element Language
    //-----------------------

    class IsWordElementWrapper( var left: Element) {

        def apply(str:String) = {

        }

        def multiple(typeStr: String) = {

            @->("element.start",left)

            left.classType = getType(typeStr)
            left.maxOccurs = 10

            @->("element.end",left)

        }

        def multiple(right: => Unit) = {

            @->("element.start",left)

            left.maxOccurs = 10

            right

            @->("element.end",left)

        }

        def is(right: => Unit) = {

            println("in is definition for Element")

            @->("element.start",left)

            right

            @->("element.end",left)

        }

       

        def as ( right: Class[_ <: Buffer]) = {

            @->("element.start",left)

            left.classType = right.getCanonicalName

            @->("element.end",left)
        }

        def as(right: String) = {

            @->("element.start",left)

            left.classType = getType(right).getCanonicalName

            @->("element.end",left)

        }
    }
    implicit def elementToIsWordWrapping(str: String) :  IsWordElementWrapper = new IsWordElementWrapper(str)

   
    // Attribute Language
    //------------------------------
    class IsWordAttributeWrapper( var left: Attribute) {

        def attribute(right: => Unit) = is(right)
        def attribute( right : String) = is(right)
        def attribute( right: Class[_ <: Buffer]) = is(right)

        def is(right: => Unit) = {

            @->("attribute.add",left)

            right

        }

        /**
            Set type of attribute based on string
        */
        def is( right : String) = {

            @->("attribute.add",left)

            // Search for type in internal map
            //--------------------------
            left.classType = getType(right).getCanonicalName

        }

        def is ( right: Class[_ <: Buffer]) = {

            @->("attribute.add",left)
            left.classType = right.getCanonicalName
        }
    }

    class AttributeWordAttributeWrapper( var left: Attribute) {


        def attribute(right: => Unit) = {

            @->("attribute.add",left)

            right

        }

        /**
            Set type of attribute based on string
        */
        def attribute( right : String) = {

            @->("attribute.add",left)

            // Search for type in internal map
            //--------------------------
            left.classType = getType(right).getCanonicalName

        }

        def attribute ( right: Class[_ <: Buffer]) = {

            @->("attribute.add",left)

            left.classType = right.getCanonicalName

        }
    }


    implicit def attributeToIsWordWrapping(attr: Attribute) :  IsWordAttributeWrapper = new IsWordAttributeWrapper(attr)
    implicit def attributeStrToAttributeWordWrapping(attr: String) :  AttributeWordAttributeWrapper = new AttributeWordAttributeWrapper(attr)
}
