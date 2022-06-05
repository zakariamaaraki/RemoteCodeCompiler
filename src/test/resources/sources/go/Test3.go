package main
import "fmt"
func main() {
    // i not declared => Compilation Error
    for (i < 10) {
        fmt.Println(i)
        i++
    }
}
