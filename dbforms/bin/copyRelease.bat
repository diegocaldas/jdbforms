@echo off
if %1.==. goto NO_USER
cd target\docs
plink -i 	 	%1@shell.sourceforge.net rm -f /home/groups/j/jd/jdbforms/htdocs/weekly/*
pscp  -i  -r *.* 	%1@shell.sourceforge.net:/home/groups/j/jd/jdbforms/htdocs
plink -i  		%1@shell.sourceforge.net chmod -R g+w /home/groups/j/jd/jdbforms/htdocs
goto ENDE
:NO_USER
echo usage copyRelease username
echo username is your sourceforge username!
echo your public key must be stored in pageant!  
goto ENDE
:ENDE