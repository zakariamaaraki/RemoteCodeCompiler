#include <stdlib.h>

int main() {
    const char *command = "ls -la /";

    // Use the system() function to execute the command
    int result = system(command);

    // Check the result of the command execution
    if (result == -1) {
        // An error occurred while trying to execute the command
        perror("Error executing command");
        return EXIT_FAILURE;
    } else {
        // Command executed successfully
        printf("Command executed successfully\n");
        return EXIT_SUCCESS;
    }
}
