# ISPyB Project

1. [Installing](#installing)
2. [Database creation and update](#database-creation-and-update)
3. [Database schema](#database-schema)

## Installing

1. Clone the ISPyB repository:

   ```
   git clone https://github.com/ispyb/ISPyB.git
   ```

2. ISPyB needs the third-party libraries provided in the `dependencies`
   directory.  These don't exist in a public repository, so install them to
   the local Maven repository so that Maven can find them:

   ```
   cd dependencies && mvn initialize
   ```

3. Build ISPyB using Maven:

   ```
   mvn clean install
   ```

   By default, ISPyB builds for the `GENERIC` site and the `development`
   environment.  These can be changed with the `ispyb.site` and `ispyb.env`
   system properties.  For example, to build for the `ESRF` site and the
   `test` environment:

   ```
   mvn -Dispyb.site=ESRF -Dispyb.env=test clean install
   ```

   If the build succeeds, a summary message will be printed like this:

   ```
   [INFO] Reactor Summary:
   [INFO]
   [INFO] ispyb-parent ...................................... SUCCESS [0.251s]
   [INFO] ispyb-ejb3 ........................................ SUCCESS [10.243s]
   [INFO] ispyb-ws .......................................... SUCCESS [1.751s]
   [INFO] ispyb-ui .......................................... SUCCESS [7.212s]
   [INFO] ispyb-ear ......................................... SUCCESS [5.048s]
   [INFO] ispyb-bcr ......................................... SUCCESS [2.217s]
   [INFO] ispyb-bcr-ear ..................................... SUCCESS [1.806s]
   ```

## Database creation and update

Run the following creation scripts from the `ispyb-ejb` module (note
that this requires the `pxadmin` database user to exist and have full
permissions):

1. `ispyb-ejb/db/scripts/pyconfig.sql`: This corresponds to the menu
   options and contains both structure and data.

2. `ispyb-ejb/db/scripts/pydb.sql`: This corresponds to the ISPyB metadata
   and contains only the database structure.

3. `ispyb-ejb/db/scripts/schemastatus.sql`: This corresponds to the
   `SchemaStatus` table and contains both structure and data.  The entries
   indicate which update scripts have been run.

4. `ispyb-ejb/db/scripts/ispybAutoprocAttachment.sql`: This corresponds to
   the type and names of different autoPROC attachments.

The creation scripts are normally updated for each tag, but if you are
using the master branch, you may have to run the update scripts in
`ispyb-ejb/db/scripts/ahead`.

Check the entries in the `SchemaStatus` table to know which scripts
to execute.  The scripts already run for the current tag are in
`ispyb-ejb/db/scripts/passed`.

### Creating an update script

The first line must be:

```sql
insert into SchemaStatus (scriptName, schemaStatus) values ('2017_06_06_blabla.sql','ONGOING');
```

Then comes the actual updates of the script:

```sql
-- SQL statements here
```

And the last line must be:

```sql
update SchemaStatus set schemaStatus = 'DONE' where scriptName = '2017_06_06_blabla.sql';
```

The last line updates the `SchemaStatus` table to mark the script as
having been run.

## Database schema

A patch or commit that changes the database schema must be accompanied
by a corresponding change to the [schema file][schema-file]
to keep it up to date.  This file can be edited using [MySQL
Workbench][mysql-workbench] (a free tool from MySQL).

[schema-file]: https://github.com/ispyb/ISPyB/blob/master/documentation/database/ISPyB_DataModel.mwb
[mysql-workbench]: https://www.mysql.com/products/workbench/
