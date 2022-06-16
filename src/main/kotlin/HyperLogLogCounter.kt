import com.google.common.hash.HashFunction
import com.google.common.hash.Hashing
import net.agkn.hll.HLL
import org.apache.commons.io.FileUtils
import org.apache.commons.io.LineIterator
import java.io.File

class HyperLogLogCounter {
    constructor() { }

    val onProgressChanged = Event<Int>()

    public fun getCount(theFile: File): Long {
        val hashFunction: HashFunction = Hashing.murmur3_128();
        val hll = HLL(14, 5); // error rate ~ 1%

        val totalFileSize = theFile.length()
        val onePercentSize: Long = totalFileSize / 100;
        var completedPercents = 1;
        var bytesRead: Long = 0
        val it: LineIterator = FileUtils.lineIterator(theFile, "UTF-8")
        try {
            while (it.hasNext()) {
                val line: String = it.nextLine()
                bytesRead += line.length;
                bytesRead += 1; // line ending symbols
                if (bytesRead > completedPercents * onePercentSize) {
                    onProgressChanged.invoke(completedPercents)
                    completedPercents += 1;
                }
                val hashedValue = hashFunction.newHasher().putBytes(line.toByteArray()).hash().asLong();
                hll.addRaw(hashedValue);
            }
        } finally {
            LineIterator.closeQuietly(it)
        }

        val cardinality = hll.cardinality();
        return cardinality
    }
}

class Event<T> {
    private val observers = mutableSetOf<(T) -> Unit>()

    operator fun plusAssign(observer: (T) -> Unit) {
        observers.add(observer)
    }

    operator fun minusAssign(observer: (T) -> Unit) {
        observers.remove(observer)
    }

    operator fun invoke(value: T) {
        for (observer in observers)
            observer(value)
    }
}