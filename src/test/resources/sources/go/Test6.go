package main
import "fmt"

func main() {

    var i int = 0
    var x int = 1

    for (i < 10) {
        x--
        fmt.Println(i / x)
        i++
    }
}
