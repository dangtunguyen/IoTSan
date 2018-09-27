# Overview

In this repository you will find the detailed technical report and protype code of IoTSan, a model-checking-based tool used to verify the safety of IoT systems.

This work was accepted and will be published at The 14th International Conference on emerging Networking EXperiments and Technologies (CoNEXT '18).

# Code Structure

IoTSan is developed based on Bandera (http://bandera.projects.cs.ksu.edu/). The "App Dependency Analyzer" module and "Translator" module are at "IoTSan/src/edu/ksu/cis/bandera/jjjc/gparser/"; The "Model Generator" module is at "IoTSan/src/edu/ksu/cis/bandera/spin/"; The "Configuration Extractor" module is at "IoTSan/src/configextractor" (This module is not yet integrated into IoTSan at this initial version).

# Implementation Note

This implementation is just the initial version of IoTSan, which does not include all modules mentioned in the paper.

# Using The Tool

First, create an Eclipse project and clone this repository (you can find the instructions at https://github.com/collab-uniba/socialcde4eclipse/wiki/How-to-import-a-GitHub-project-into-Eclipse).

Second, create the folder "input/smartapps" at the root directory of your porject. You then need to put the source code of apps and their configuration files in this folder. Note that you should give good names to app files since they will become the names of apps in the resulting modeled system.

Third, run the "IotSan.java" as "Java Application". The resulting Promela model code will be generated and stored at ".../IoTSan/output/IotSanOutput/birc".

Fourth, append the safety property in LTL format to the end of each output file.

Sixth, run the verification with SPIN using the following command in a terminal: spin -search -DVECTORSZ=36736 -DSAFETY -DBITSTATE -E -NOBOUNDCHECK -NOFAIR -NOCOMP -n -w36 <Promela file>. An example of a command is: spin -search -DVECTORSZ=36736 -DSAFETY -DBITSTATE -E -NOBOUNDCHECK -NOFAIR -NOCOMP -n -w36 SmartThing0.prom

Seventh, if any violation is detected, the file "SmartThing0.prom.trail" will be created by SPIN. Use the following command to get counter-example log: spin -p -replay SmartThings0.prom > log.txt

Eight, get a filtered logs by using: grep -v 'allEvtsHandled' log.txt | grep -v 'generatedEvent.EvtType = g_' |  grep -E 'generatedEvent.EvtType =|ST_Command.EvtType =|BroadcastChans|Handle|assert|location.mode'  > filterLog.txt

Please refer to the directory "IoTSan/samples/1" for an example of app source code, config files, and log files.

Note that you need to install SPIN by following instructions at http://spinroot.com/spin/whatispin.html before using this tool.

# Citation
If you use the source code, please cite the following paper:

Dang Tu Nguyen, Chengyu Song, Zhiyun Qian, Srikanth V. Krishnamurthy, Edward J. M. Colbert, and Patrick McDaniel, "IotSan: Fortifying the Safety of IoT Systems," In Proc. of the 14th International Conference on emerging Networking EXperiments and Technologies (CoNEXT '18)
