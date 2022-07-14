import requests
import threading

# C
cInput = open("inputs/amShZWinsABet-1.txt", "r").read()
cExpectedOutput = open("expected-outputs/amShZWinsABet-1.txt", "r").read()
cSourceCode = open("source-code/AmShZWinsABet.c", "r").read()

cData = {
    "expectedOutput": cExpectedOutput,
    "input": cInput,
    "language": "C",
    "memoryLimit": 1500,
    "sourceCode": cSourceCode,
    "timeLimit": 15
}

# CPP
cppInput = open("inputs/physEdOnline-1.txt", "r").read()
cppExpectedOutput = open("expected-outputs/physEdOnline-1.txt", "r").read()
cppSourceCode = open("source-code/PhysEdOnline.cpp", "r").read()

cppData = {
    "expectedOutput": cppExpectedOutput,
    "input": cppInput,
    "language": "CPP",
    "memoryLimit": 1500,
    "sourceCode": cppSourceCode,
    "timeLimit": 15
}

# Java
javaInput = open("inputs/Watermelon-1.txt", "r").read()
javaExpectedOutput = open("expected-outputs/Watermelon-1.txt", "r").read()
javaSourceCode = open("source-code/Watermelon.java", "r").read()

javaData = {
    "expectedOutput": javaExpectedOutput,
    "input": javaInput,
    "language": "JAVA",
    "memoryLimit": 1500,
    "sourceCode": javaSourceCode,
    "timeLimit": 15
}

# Python
pythonInput = open("inputs/makeEven-1.txt", "r").read()
pythonExpectedOutput = open("expected-outputs/makeEven-1.txt", "r").read()
pythonSourceCode = open("source-code/MakeEven.py", "r").read()

pythonData = {
    "expectedOutput": pythonExpectedOutput,
    "input": pythonInput,
    "language": "PYTHON",
    "memoryLimit": 1500,
    "sourceCode": pythonSourceCode,
    "timeLimit": 15
}

URL = "http://localhost:8080/api/compile/json"
headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}

class Result:
    def __init__(self, statusResponse, statusCode, output, error, expectedOutput, executionDuration):
        self.statusResponse = statusResponse
        self.statusCode = statusCode
        self.output = output
        self.error = error
        self.expectedOutput = expectedOutput
        self.executionDuration = executionDuration

class Response:
    def __init__(self, result, dateTime):
        self.result = result,
        self.dateTime = dateTime


class cthread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    # helper function to execute the threads
    def run(self):
        requests.post(url = URL, json = cData, headers = headers)

class cppthread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    # helper function to execute the threads
    def run(self):
        requests.post(url = URL, json = cppData, headers = headers)

class pythonthread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    # helper function to execute the threads
    def run(self):
        requests.post(url = URL, json = pythonData, headers = headers)


class javathread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    # helper function to execute the threads
    def run(self):
        requests.post(url = URL, json = javaData, headers = headers)

for i in range(10):
    # C
    cRequestThread = cthread()
    cRequestThread.start()

    # CPP
    cppRequestThread = cppthread()
    cppRequestThread.start()

    # Python
    pythonRequestThread = pythonthread()
    pythonRequestThread.start()

    # Java
    javaRequestThread = javathread()
    javaRequestThread.start()