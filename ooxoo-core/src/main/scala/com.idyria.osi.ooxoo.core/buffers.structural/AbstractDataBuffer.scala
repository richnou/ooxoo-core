/**
 *
 */
package com.idyria.osi.ooxoo.core.buffers.structural

import scala.beans.BeanProperty
import com.idyria.osi.ooxoo.core.buffers.structural.io.IOBuffer

import com.idyria.osi.tea.logging.TLog

/**
 *
 * This class is a base class for all Buffers that hold a single piece of data
 *
 * For example, all the implementations of default XSD data types are data buffers
 * A data buffer requires the implementation of string de/serialisation for streamOut
 *
 *
 *
 * @author rleys
 *
 */
abstract class AbstractDataBuffer[DT <: AnyRef]
				(
				    // Variable for local Data
					@BeanProperty()
					var data : DT = null ) extends BaseBuffer {


  def dataToString : String
  def dataFromString( str : String) : DT


  // Data Set
  //------------------

  /**
    Updates Internal value, and progagates
  */
  def set( data : DT ) = {

    // Set
    this.data = data

    // Propagate
    this.push

  }

  // Propagate
  //-----------------


  // Stream
  //--------------


 /**
  * Create data unit using string conversion
  */
 override def createDataUnit: DataUnit = {

    // Create Empty Data Unit
    //------------------
    var du = new DataUnit
    du.setValue(this.dataToString)
    du

  }


  /**
   * streamIn data:
   * 	- Convert from string to local type
   *  	-
   */
  override def streamIn(du : DataUnit) = {

    // If we have a hierarchy close data unit -> remove end IO buffer because we are done here
    //----------------------------
    if (du.attribute==null && du.element==null && du.hierarchical==false && du.value==null) {
      TLog.logFine("---- End of hierarchy for data buffer ("+this.getClass()+") -> remove IO chain");
      TLog.logFine("---- BCBefore: "+this.printForwardChain)
      if (this.lastBuffer.isInstanceOf[IOBuffer])
    	  this.lastBuffer.remove
	  TLog.logFine("---- BCAfter: "+this.printForwardChain)

    }

    // Otheerwise, if we have a value, -> import data from string
    //------------
    if (du.value!=null) {
      var res = this.dataFromString(du.value)
      this.set(res)
    }

    // Let parent do the job
    super.streamIn(du)

  }



}