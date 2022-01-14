package com.isma3il.core.model

interface Mapper<I,O> {
   fun map(input:I?):O
}