using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1
{
    class Program
    {
        static void Main(string[] args)
        {
            int i = 0;
            long[] T = new long[1000000000];
            while (i < 10) {
                T[i] = i;
                Console.WriteLine(i++);
            }

        }
    }
}
