package com.idyria.osi.ooxoo.core.buffers.structural

import com.idyria.osi.ooxoo.core.buffers.datatypes.XSDStringBuffer
import scala.reflect.runtime.universe._
import scala.reflect.ClassTag

trait AnyBufferTrait extends Buffer {

    var name : String = null
    
    var ns : String = null
}

@any
class AnyElementBuffer extends ElementBuffer with AnyBufferTrait {

    @any 
    var content = AnyXList()

}

@any
class AnyAttributeBuffer extends XSDStringBuffer with AnyBufferTrait {


}

/**
    Trait to add any content variable to a class
*/
trait AnyContent {

    @any
    var content = AnyXList()

}

/*class AnyXList( cl: => AnyElementBuffer) extends XList[AnyElementBuffer](AnyXList) {

}*/

object AnyXList {

    // 

    /**
        Map Containing possible Modeled XML elements that could be instanciated by the XList closure 
        instead of creating generic Any classes

        Map Format: 

            ( ns -> ElementName) -> Class

        Arguments:
            - ns: The namespace of element, or an empty string if no namespace
            - the name of the XML element
            - The class implementing ElementBuffer for that element

        @warning The model class MUST have an empty constructor, otherwise a runtime error will be thrown
    */
    var modelsMap = Map[ Tuple2[String,String], ( DataUnit => Buffer )]()

    /**
        Register a new model in the models map
        
        Models are registered as namespaced and non-namespaced.
        Use namespaces in case it is unclear if any collisions happen

        @throws IllegalArgumentException if cannot determine xelement parameters from class
    */
    def apply[T <: Buffer]( cl : Class[T]) = {

        //println("Registering model class in XList")

        // Get name and ns from annotation
        //-------------
        var xelement = xelement_base(cl)
        if (xelement==null) {
            throw new IllegalArgumentException(s"Cannot register XML model class $cl that seems to be missing @xelement annotation")
        }

        // Register
        //------------------
        this.modelsMap = this.modelsMap +((xelement.ns -> xelement.name) -> {du => cl.newInstance()}) 
        this.modelsMap = this.modelsMap +(("" -> xelement.name) -> {du => cl.newInstance()}) 
    }
    
    def register[T  <: Buffer](implicit tag: ClassTag[T]) = {
      AnyXList(tag.runtimeClass.asInstanceOf[Class[T]])
    }

    def register[T  <: Buffer](cl: => T)(implicit tag: ClassTag[T]) = {
      
      // Get name and ns from annotation
    //-------------
    var xelement = xelement_base(tag.runtimeClass.asInstanceOf[Class[T]])
    if (xelement==null) {
        throw new IllegalArgumentException(s"Cannot register XML model class $cl that seems to be missing @xelement annotation")
    }

    // Register
    //------------------
    this.modelsMap = this.modelsMap + ((xelement.ns -> xelement.name) -> { du ⇒ cl })
    this.modelsMap = this.modelsMap + (("" -> xelement.name) -> { du ⇒ cl })

  }
    
    def apply() = {
        
        /**
            Creating an XList of anyBuffer
            The Buffers are setup depending on data unit provided
        */
        XList[Buffer] {
            du : DataUnit => 

              
              	//println("Got Buffer for this Header content")
              	
              	var res : Buffer = null
                (du.element,du.attribute) match {
                    
                    // Element
                    //------------
                    case (element,null) => 

                      	//println(s"AnyXList got a DU to translate to buffer: ${element.name} ${element.ns}")
                      
                        // Is there a registered model for the element ?
                        //-------------
                        modelsMap.get((element.ns -> element.name)) match {

                            //-> Yes
                            case Some(builder) => 

                                //-- Instanciate
                                try {
                                   res = builder(du)
                                    
                                   //println(s"Created from model class: ${res}")
                                   
                                } catch {
                                    case e: Throwable => 
                                      e.printStackTrace()
                                      throw new RuntimeException(s"The Any content list found a model for element: ${element.ns}:${element.name}, but the model instance could not be created, does it have an empty constructor? if not, you MUST add one ",e)
                                }

                            //-> No, create a generic element
                            case None => 

                                var elementBuffer = new AnyElementBuffer
                                elementBuffer.name = element.name
                                elementBuffer.ns = element.ns
                                res = elementBuffer
                        }

                        

                    // Attribute
                    //----------------
                    case (null,attribute) =>

                        var attributeBuffer = new AnyAttributeBuffer
                        attributeBuffer.name = attribute.name
                        attributeBuffer.ns = attribute.ns match {
                          case null => null
                          case "" => null
                          case ns => ns
                        }
                        res = attributeBuffer

                       //null 

                    case _ => null
                }
        
              	// Return
              	res
                
                 
        }

    }

}
