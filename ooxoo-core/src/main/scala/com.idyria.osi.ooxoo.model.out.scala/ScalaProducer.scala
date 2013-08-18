package com.idyria.osi.ooxoo.model.out.scala

import com.idyria.osi.ooxoo.model._
import com.idyria.osi.ooxoo.core.buffers.structural._

/**
    This Producer creates scala class implementations for the models

*/
class ScalaProducer extends Producer {

    /**
        The output package

        Model parameter: scalaProducer.targetPackage
    */
    var targetPackage : String = ""

    def produce(model: Model, out : Writer) = {


        // Try to find Target Package from model
        //------------------
        model.parameter("scalaProducer.targetPackage") match {
            case Some(p) => this.targetPackage = p
            case None =>
        }

        def writeElement( element : Element ) : Unit = {

            // Check Name
            //-------------------
            var namespace = ""
            var name = element.name
            model.splitName(element.name) match {
                case (sNs,sName) => namespace = sNs ; name = sName
            }
            /*model.namespace(element.name) match {
                case Some(foundNamespace) => 
                    namespace = foundNamespace
                    name = element.name.split(":")(1)
                case None =>
            }*/

            // Write File
            //-----------------------

            out.file("./"+targetPackage+"/"+name+".scala")

            //-- Package
            out << s"""package $targetPackage
            """

            //-- Import
            out << s"""
import ${classOf[ElementBuffer].getCanonicalName}
import ${classOf[XList[_]].getCanonicalName}
import ${classOf[xattribute].getCanonicalName}
import ${classOf[xelement].getCanonicalName}
            """
            
            //-- Class Definition
            (namespace,name) match {
                case ("",name) =>  out << s"""@xelement(name="$name")"""
                case (namespace,name) =>  out << s"""@xelement(name="$name",ns="$namespace")"""
            }
           
            //-- Imported Traits
            var traits =""
            element.traits.foreach { t => traits+=s"with $t "  }

            //-- End of class start
            out << s"""class $name extends ${element.classType} $traits {
            """

            //-- Attributes
            out.indent
            element.attributes.foreach { attribute => 

                // Annotation
                var resolvedName = model.splitName(attribute.name)
                resolvedName match {
                    case ("",name) =>  

                        out << s"""@xattribute(name="$name")"""
                    case (namespace,name) =>  

                        out << s"""@xattribute(name="$name",ns="$namespace")"""
                }

                attribute.maxOccurs match {

                    case count  if (count>1) =>

                        out << s"""var ${resolvedName._2.toLowerCase} = XList { new ${attribute.classType}}
                        """

                    case _ => 

                        out << s"""var ${resolvedName._2.toLowerCase} : ${attribute.classType} = null
                        """
                }
                
            
            }
            out.outdent

            //-- Sub Element
            out.indent
            element.elements.foreach { element =>

                // Annotation
                var resolvedName = model.splitName(element.name)
                resolvedName match {
                    case ("",name) =>  
                        out << s"""@xelement(name="$name")"""

                    case (namespace,name) =>  
                    
                        out << s"""@xelement(name="$name",ns="$namespace")"""
                }

                // Element definition
                element.maxOccurs match {


                    case count if (count>1) =>
         
                        out << s"""var ${resolvedName._2.toLowerCase} = XList { new $targetPackage.${resolvedName._2}}
                        """
 
                    case _ =>

                        out << s"""var ${resolvedName._2.toLowerCase} : $targetPackage.${resolvedName._2} = null
                        """
                }
            }
            out.outdent

            //-- End of class
            out << s"""}"""
            out.finish

            // Output Sub Elements
            //---------------------
            element.elements.foreach(writeElement(_))
        }

        model.topElements.foreach {writeElement(_)}

    }
}