/*
 * #%L
 * Core runtime for OOXOO
 * %%
 * Copyright (C) 2008 - 2014 OSI / Computer Architecture Group @ Uni. Heidelberg
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package com.idyria.osi.ooxoo.core.buffers.datatypes

import com.idyria.osi.ooxoo.core.buffers.structural.AbstractDataBuffer
import scala.language.implicitConversions
import com.idyria.osi.ooxoo.core.buffers.structural.DataUnit

/**
 * Buffer used to define a string
 * @author rleys
 *
 */
class XSDStringBuffer extends AbstractDataBuffer[String] with Comparable[String] {

  def this(str: String) = {
    this();
    str match {
      case null =>
      case s => this.data = dataFromString(s)
    }

  }

  def dataToString: String = {
    this.data

  }

  /**
   * Set provided string to actual data
   */
  def dataFromString(str: String): String = str match {
    case null => null
    case s if (data==null) => 
      //println(s"Data from strng with: "+str)
      /*try {
        throw new RuntimeException("test")
      }catch {
        case e: Throwable => e.printStackTrace(System.out)
      }*/
      //this.data = str.trim; 
      //data
       str.trim
    case s => 
     /* println(s"Data from strng with: "+str)
       try {
        throw new RuntimeException("test")
      }catch {
        case e: Throwable =>  e.printStackTrace(System.out)
      }*/
      this.data + s.trim

      //data
  }

  override def toString: String = {
    if (this.data == null)
      super.toString
    this.data

  }

  def equals(comp: XSDStringBuffer): Boolean = {
    //println("Called equals to xsdstringbuffer")
    this.data.equals(comp.data)
  }

  def equals(comp: String): Boolean = {

    //println("Called equals to String")
    this.data == comp
  }

  def compareTo(comp: String): Int = {

    //println("Called compare to to xsdstringbuffer")
    this.data.compareTo(comp)
  }

  def ===(str: String): Boolean = {
    // println(s"in equals compare")
    this.data == str
  }

  /* implicit def convertSubClassesToStringBufferType[T <: XSDStringBuffer](str:String) : T = {
    

   var r = this.getClass.newInstance()
    r.dataFromString(str)
   r.asInstanceOf[T]
  }*/

}
object XSDStringBuffer {

  def apply(str: String) = new XSDStringBuffer(str)
  def apply() = new XSDStringBuffer

  implicit def convertAnyToXSDStringBuffer(str: Any): XSDStringBuffer = new XSDStringBuffer(str.toString)
  implicit def convertStringToXSDStringBuffer(str: String): XSDStringBuffer = new XSDStringBuffer(str)
  implicit def convertXSDStringBufferToString(str: XSDStringBuffer): String = {
    str match {
      case null => ""
      case other => other.toString()
    }
  }

  def convertFromString(data: String): XSDStringBuffer = new XSDStringBuffer(data)
}

class CDataBuffer extends XSDStringBuffer {

  def this(str: String) = { 
    this(); 
    str match {
      case null =>
      case s => 
        this.data = dataFromString(s)
    }
    
 }

  /**
   * Override streamout to add cdata parameter to data unit
   */
  override def streamOut(du: DataUnit) = {

    du("cdata" -> true)

    super.streamOut(du)
  }

}
object CDataBuffer {

  def apply(str: String) = new CDataBuffer(str)
  def apply() = new CDataBuffer
  implicit def convertStringToCDataBuffer(str: String): CDataBuffer = new CDataBuffer(str)
  def convertFromString(data: String): CDataBuffer = new CDataBuffer(data)

}

class StringMapBuffer extends MapBuffer[XSDStringBuffer]({ du => new XSDStringBuffer })

object StringMapBuffer {
  def apply() = new StringMapBuffer
}
