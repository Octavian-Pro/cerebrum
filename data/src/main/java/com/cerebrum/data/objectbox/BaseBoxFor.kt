package com.cerebrum.data.objectbox

import io.objectbox.Box

abstract class BaseBoxFor<T>(entityClass: Class<T>) {
  protected val box: Box<T> by lazy {
    ObjectBox.store.boxFor(entityClass)
  }
  val items: List<T>
    get() = box.all

  fun contains(id: Long) = box.contains(id)
  fun remove(id: Long) {
    box.remove(id)
  }

  fun removeAll() {
    box.removeAll()
  }

  fun get(id: Long): T? = box.get(id)

  fun add(value: T & Any) {
    box.put(value)
  }
}