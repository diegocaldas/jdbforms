@echo off
if %1.==. goto NO_USER
call maven dbforms:getrelease
cd target\release\dbforms
call maven dbforms:deploy
cd target\docs
plink 	       %1@shell.sourceforge.net rm -f /home/groups/j/jd/jdbforms/htdocs/weekly/*
pscp   -r *.*        %1@shell.sourceforge.net:/home/groups/j/jd/jdbforms/htdocs
plink  	       %1@shell.sourceforge.net chmod -R g+w /home/groups/j/jd/jdbforms/htdocs
goto ENDE
:NO_USER
echo usage buildRelease username
echo username is your sourceforge username!
echo your public key must be stored in pageant!  
goto ENDE
:ENDE