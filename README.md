Please create a back-end API that manages a category hierarchy tree:
=========================

1. Create a database entity called Category, which has an id, a name, parent category, and child
categories. The hierarchy will be n-deep.
2. Create a back-end restful API endpoint that can manage CRUD operations for categories.
Please use Java, Spring, Spring boot, Spring JPA, Spring Data JPA

NOTE: Your code will be reviewed and during the phone interview you will be asked about decisions
made. You may also be asked to make modifications to the code during the interview to enhance it or
simplify it.

TODO:
=======================
1) clean up imports
2) More tests!
3) Make it so you can add a child to a parent without removing other children
4) Why did I have to tell Json to ignore children for the tests to work?
5) Prevent circular hierarchy
6) Error handling
7) Validation
8) UI