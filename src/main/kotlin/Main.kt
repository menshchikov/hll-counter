import java.io.File

fun main(args: Array<String>) {
    if(args.isNullOrEmpty()){
        println("Input file path as first argument.")
        return
    }
    val theFile = File(args[0]);
    val hllc = HyperLogLogCounter();
    hllc.onProgressChanged += {
        println("${it}%")
    }
    val cardinality = hllc.getCount(theFile)
    println("cardinality: ${cardinality.toString()}");

}

