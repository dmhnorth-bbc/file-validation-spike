# file-validation-spike
UGCUPLOAD-20 Experimentation with inspection of file types

The Apache Tika library looks to be a good one for usage on this project. Native java comes with certain complexities and can end up pushing out null results.

Contained in this repo is an example and a selection of files for testing. The tests don't all pass as it's a very naive implementation just to check we aren't getting null results/some sort of information is coming back for the file types we're interested in. Also, this is only really to be used as a note for when we come back to use the library.

'FileTypeValidationWithSpecificImages' is a test file which contains files with all the image file types we are expecting as part of MVP. Both with correct and incorrect file extensions.
Assertions are based on a return of 'image/*'
Note: PDF counts as an 'application/pdf'