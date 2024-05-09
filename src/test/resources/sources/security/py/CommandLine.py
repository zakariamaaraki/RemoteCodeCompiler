import os

def execute_command(command):
    try:
        # Execute the command
        result = os.popen(command)

        # Read the output
        output = result.read()

        # Check if the command was successful
        if result.close() is None:
            # Print the output
            print("Output:")
            print(output)
        else:
            # Print error message
            print("Error executing command")
    except Exception as e:
        print("An error occurred:", e)

# Command to execute
command = "ls"

# Call the function to execute the command
execute_command(command)