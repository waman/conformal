package org.waman.conformal.linear

trait Matrix[K]{

  def rank: Int

  def elements: Seq[K]

  def rowCount: Int
  def rows: Seq[Seq[K]]

  def columnCount: Int
  def columns: Seq[Seq[K]]

  def determinant: K

  //***** Operation *****
  def *(other: Matrix[K]): Matrix[K]
  def +(other: Matrix[K]): Matrix[K]
  def -(other: Matrix[K]): Matrix[K]
}

trait SquareMatrix[K] extends Matrix[K]{
  def order: Int

  def rowCount = order
  def columnCount = order
}
