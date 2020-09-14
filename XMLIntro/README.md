The files in this folder come from the book XML, second edition by Kevin Howard Goldberg, with my edits and comments.

To see the results of some of these files, you may need to run Chrome with less security. Here is how to do that:



Command Prompt:

Navigate to Chrome (for me this means entering the following: C:\Program Files (x86)\Google\Chrome\Application)

Then prompt chrome.exe --disable-web-security --user-data-dir="C:/ChromeDevSession"



To Create a shortcut to Chrome with the security disabled: 

Desktop -> New -> Shortcut

"C:\Program Files (x86)\Google\Chrome\Application\chrome.exe" (or your path to chrome) --disable-web-security --disable-gpu --user-data-dir="C:/ChromeDevSession"
