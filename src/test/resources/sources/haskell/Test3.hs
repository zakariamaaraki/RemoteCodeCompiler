error :: [Char] -> a
error s = raise# (errorCallException s)