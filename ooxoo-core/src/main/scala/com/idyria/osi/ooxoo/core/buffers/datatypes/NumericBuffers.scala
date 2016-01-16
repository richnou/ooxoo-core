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

/**
 * Buffer to represent an Integer
 *
 */
class IntegerBuffer extends AbstractDataBuffer[Integer] with Comparable[Int] {

  this.data = 0
  
  def dataFromString(str: String): Integer = {
    this.data = Integer.decode(str)
    this.data
  }

  def dataToString: String = if (data != null) this.data.toString() else "No value"

  override def toString: String = this.dataToString

  def equals(comp: IntegerBuffer): Boolean = this.data == comp.data

  def compareTo(comp: Int): Int = this.data.compareTo(comp)

}

object IntegerBuffer {

  def apply() = new IntegerBuffer

  def apply(value: Integer) = {
    var res = new IntegerBuffer
    res.data = value
    res
  }
  
  def convertFromString( str : String) : IntegerBuffer = {
    
    var b = new IntegerBuffer
    b.dataFromString(str)
    b
  }

  implicit def convertIntegerToIntegerBuffer(value: Integer): IntegerBuffer = IntegerBuffer(value)
  implicit def convertIntegerBufferToInteger(buffer: IntegerBuffer): Integer = buffer.data

  implicit def convertIntToIntegerBuffer(value: Int): IntegerBuffer = IntegerBuffer(value)
  implicit def convertIntegerBufferToInt(buffer: IntegerBuffer): Int = buffer.data

}

/**
 * Buffer to represent a Long
 *
 */
class LongBuffer extends AbstractDataBuffer[java.lang.Long] with Comparable[java.lang.Long] {

  this.data = 0
  
  def dataFromString(str: String): java.lang.Long = {
    
    str.trim match {
      case "" =>
      case r => 
        this.data = java.lang.Long.decode(r)
    }
    
    return this.data
  }

  def dataToString: String = if (data != null) this.data.toString() else null

  override def toString: String = this.dataToString

  def equals(comp: LongBuffer): Boolean = this.data == comp.data

  def compareTo(comp: java.lang.Long): Int = this.data.compareTo(comp)

  def +(add: Long): Long = this.data + add
}

object LongBuffer {

  def apply() = new LongBuffer

  def apply(value: java.lang.Long) = {
    var res = new LongBuffer
    res.data = value
    res
  }

  def apply(value: Long) = {
    var res = new LongBuffer
    res.data = value
    res
  }
  
  def convertFromString( str : String)  = {
    
    var b = new LongBuffer
    b.dataFromString(str)
    b
  }

  implicit def convertLongToLongBuffer(value: java.lang.Long): LongBuffer = LongBuffer(value)
  implicit def convertLongBufferToLong(buffer: LongBuffer): java.lang.Long = buffer.data

  implicit def convertLong2ToLongBuffer(value: Long): LongBuffer = LongBuffer(value)
  implicit def convertLongBufferToLong2(buffer: LongBuffer): Long = buffer.data
  
  
}

/**
 * Buffer to represent a Double
 *
 */
class DoubleBuffer extends AbstractDataBuffer[java.lang.Double] with Comparable[java.lang.Double] {

  this.data = 0.0
  
  def dataFromString(str: String): java.lang.Double = {
    this.data = java.lang.Double.parseDouble(str)
    this.data
  }

  def dataToString: String = if (data != null) this.data.toString() else null

  override def toString: String = this.dataToString

  def equals(comp: DoubleBuffer): Boolean = this.data == comp.data

  def compareTo(comp: java.lang.Double): Int = this.data.compareTo(comp)

}

object DoubleBuffer {

  def apply() = new DoubleBuffer

  def apply(value: java.lang.Double) = {
    var res = new DoubleBuffer
    res.data = value
    res
  }
  
  def convertFromString( str : String)  = {
    
    var b = new DoubleBuffer
    b.dataFromString(str)
    b
  }

  implicit def convertDoubleToDoubleBuffer(value: java.lang.Double): DoubleBuffer = DoubleBuffer(value)
  implicit def convertDoubleBufferToDouble(buffer: DoubleBuffer): java.lang.Double = buffer.data

}

/**
 * Buffer to represent a Float
 *
 */
class FloatBuffer extends AbstractDataBuffer[java.lang.Float] with Comparable[java.lang.Float] {

  this.data = 0.0f
  
  def dataFromString(str: String): java.lang.Float = {
    this.data = java.lang.Float.parseFloat(str)
    this.data
  }

  def dataToString: String = if (data != null) this.data.toString() else null

  override def toString: String = this.dataToString

  def equals(comp: FloatBuffer): Boolean = this.data == comp.data

  def compareTo(comp: java.lang.Float): Int = this.data.compareTo(comp)

}

object FloatBuffer {

  def apply() = new FloatBuffer

  def apply(value: java.lang.Float) = {
    var res = new FloatBuffer
    res.data = value
    res
  }

  def convertFromString( str : String)  = {
    
    var b = new FloatBuffer
    b.dataFromString(str)
    b
  }
  
  implicit def convertFloatToFloatBuffer(value: java.lang.Float): FloatBuffer = FloatBuffer(value)
  implicit def convertFloatToFloatBuffer(value: Float): FloatBuffer = FloatBuffer(value)
  implicit def convertFloatBufferToFloat(buffer: FloatBuffer): java.lang.Float = buffer.data
  implicit def convertFloatBufferToScalaFloat(buffer: FloatBuffer): Float = buffer.data

}