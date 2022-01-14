package com.isma3il.core.base

abstract class BaseUseCase<I,O> {
  abstract fun execute(input:I?):O
}