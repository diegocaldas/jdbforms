@echo off
if %1.==. goto NO_USER
call maven dbforms:getrelease
cd temp\release\dbforms
call maven dbforms:deploy
cd target\docs
plink -i "%SF_PRIVKEY%" 	%1@shell.sourceforge.net rm -f /home/groups/j/jd/jdbforms/htdocs/weekly/*
pscp  -i "%SF_PRIVKEY%" -r *.*  %1@shell.sourceforge.net:/home/groups/j/jd/jdbforms/htdocs
plink -i "%SF_PRIVKEY%" 	%1@shell.sourceforge.net chmod -R g+w /home/groups/j/jd/jdbforms/htdocs
goto ENDE
:NO_USER
echo usage copyRelease username
echo username is your sourceforge username!
echo your public key must be stored in pageant!  
goto ENDE
:ENDE