package vn.unicloud.genericqueue.server.utils.mapping

import java.nio.ByteBuffer
import java.util.*


fun UUID.toBytes(): ByteArray {
    val bb: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
    bb.putLong(this.mostSignificantBits)
    bb.putLong(this.leastSignificantBits)
    return bb.array()
}

fun ByteArray.toUUID(): UUID {
    val byteBuffer = ByteBuffer.wrap(this)
    val high = byteBuffer.long
    val low = byteBuffer.long
    return UUID(high, low)
}