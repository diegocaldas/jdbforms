@echo off
if "%SF_PRIVKEY%".==. goto NO_PRIVKEY
if %SF_USER%.==. goto NO_USER
call maven dbforms:getrelease
cd temp\release\dbforms
call maven dbforms:deploy
cd doc
plink -i "%SF_PRIVKEY%" %SF_USER%@shell.sourceforge.net rm -f /home/groups/j/jd/jdbforms/htdocs/weekly/*
pscp  -i "%SF_PRIVKEY%" -r *.* %SF_USER%@shell.sourceforge.net:/home/groups/j/jd/jdbforms/htdocs
plink -i "%SF_PRIVKEY%" %SF_USER%@shell.sourceforge.net chmod -R g+w /home/groups/j/jd/jdbforms/htdocs
goto ENDE
:NO_PRIVKEY
echo enviroment variable SF_PRIVKEY not set, set it to your SourceForge private key file first!
goto ENDE
:NO_USER
echo enviroment variable SF_USER not set, set it to your SourceForge user name first!
goto ENDE
:ENDE