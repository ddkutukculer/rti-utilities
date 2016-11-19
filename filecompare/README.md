#DESCRIPTION
This application uses two input files(drainer.log.file and ocs.ccs.log.file) and
compares it to find out if all the triggers in ocs.ccs.log.file are processed and
 exist in the other file(drainer.log.file).
Reports the result in a file if requested.



##USAGE
1. prepare trigger log file: need to filter lines with trigger definitions from ocs_ccs.log files

  - for each ocs_ccs*.log:
    RUN: -> grep protocolName ocs_ccs.log >> triggers_ocs_ccs.log

    ex. grep protocolName ocs_ccs.log.5 >> triggers_ocs_ccs.log
        grep protocolName ocs_ccs.log.4 >> triggers_ocs_ccs.log
        grep protocolName ocs_ccs.log.3 >> triggers_ocs_ccs.log
        grep protocolName ocs_ccs.log.2 >> triggers_ocs_ccs.log
        grep protocolName ocs_ccs.log.1 >> triggers_ocs_ccs.log
        grep protocolName ocs_ccs.log >> triggers_ocs_ccs.log
        .
        .
  - merge new files from different web service instances into one consolidated file.

2. prepare drainer output file
    RUN: -> java -jar rabbit-drainer*.jar
    - merge all drainer output into one consolidated file based on the date

3. Note the created file paths:
  - drainer.log.file=?
  - ocs.ccs.log.file=?

4. Run the sanitychecker app.
  - It takes 4 parameters:
    - drainer.log.file: path to drainer output file that contains processed triggers.
        ex. /home/rti/tmp/drain/filtered/filtered-messages.log
    - ocs.ccs.log.file: path to log file that contains incoming trigger definitions.
        ex. /opt/atos/var/log/triggers_ocs_ccs.log
    - report: boolean flag to determine creating a report after comparison.
        TRUE if a report output is expected. FALSE if only numbers of the result is sufficient.
    - output.dir: path to output directory. Two files will be created under this
        folder: missingTriggers_<DATE>.log and validatedTriggers_<DATE>.log

  -> java -jar  -Ddrainer.log.file=??? -Docs.ccs.log.file=??? -Dreport=??? -Doutput.dir=????  rti-sanitycheck-1.0.0.jar
