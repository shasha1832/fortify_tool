# LineEditor

LineEditor is a simple line-oriented text editor implemented in Java. It reads a text file and allows basic editing commands such as listing lines, deleting a line, inserting a line, and saving changes.

## Usage

To run the LineEditor, use the following command:
java LineEditor <file_path>

How to use ?
java LineEditor testfile.txt
>> list
1: first line
2: second line
3: last line
>> del 2
>> list
1: first line
2: last line
>> ins 2 new line
>> list
1: first line
2: new line
3: last line
>> save
>> quit

