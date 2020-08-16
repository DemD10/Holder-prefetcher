package com.app.holderprefetcher

class Model(val someText: String) {

    companion object {
        fun createList(count: Int) : List<Model> {
            val result = mutableListOf<Model>()
            for (i in 0..count) {
                result.add(Model(i.toString()))
            }
            return result
        }
    }
}