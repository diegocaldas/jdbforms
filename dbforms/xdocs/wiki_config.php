<?php
 
#--- change timezone to GMT
putenv ("TZ=GMT"); 

#-- change dir to where this config script is located
chdir(dirname(__FILE__)."/_ewiki");



#-- OPEN DATABASE for ewiki
#
echo "connect";
if (function_exists("mysql_connect")) {
   echo "connect2";
   $ok = mysql_connect("mysql4-j", "j17608rw", "db4712forms-rw") &&  mysql_select_db('j17608_dbforms');
}

#-- or use the flat file database backend as fallback
if (!$ok) {
   // define("EWIKI_DBFILES_DIRECTORY", "/tmp");
   include("plugins/db/flat_files.php");
}


#-- constants
define("EWIKI_NAME", 'DbFormsWiki');
define("EWIKI_PAGE_INDEX",   'WiKi');
define("EWIKI_SCRIPT", "http://jdbforms.sourceforge.net/wiki.php?page=");
define("EWIKI_LOGFILE", '/home/groups/j/jd/jdbforms/htdocs/_log/ewiki.txt');
define("EWIKI_TMP", "/home/groups/j/jd/jdbforms/htdocs/tmp");
#define("EWIKI_TMP", "/tmp");
define("EWIKI_LOGLEVEL", '9');
define("EWIKI_EDIT_REDIRECT", 0);
define("EWIKI_NOTIFY_WITH_DIFF", 1);
define("EWIKI_PROTECTED_MODE", 1);
#define("EWIKI_PROTECTED_MODE_HIDING", 1);
define("EWIKI_AUTO_LOGIN", 1);


#-- set a few configuration variables
$ewiki_config["edit_thank_you"] = 0;

#-- load plugins
include("plugins/lib/mime_magic.php");

include("plugins/email_protect.php");
#include("plugins/spages.php");
include("plugins/jump.php");
include("plugins/notify.php");
include("plugins/patchsaving.php");

include("plugins/feature/appendonly.php");
#include("plugins/feature/imgresize_gd.php");
include("plugins/feature/imgfile_naming.php");

#include("plugins/mpi/mpi.php");


include("plugins/page/powersearch.php");
include("plugins/page/pageindex.php");
include("plugins/page/wordindex.php");
#include("plugins/page/orphanedpages.php");
#include("plugins/page/wantedpages.php");
#include("plugins/page/hitcounter.php");
include("plugins/page/textupload.php");
include("plugins/page/recentchanges.php");
include("plugins/page/wikinews.php");
include("plugins/page/sitemap.php");
#include("plugins/page/wikiuserlogin.php");

include("plugins/action/diff.php");
include("plugins/action/diff_gnu.php");
include("plugins/action/info_qdiff.php");

include("plugins/aview/downloads.php");
include("plugins/module/downloads.php");

include("plugins/markup/css.php");
include("plugins/markup/naturallists.php");

include("fragments/strip_wonderful_slashes.php");
#include("fragments/strike_register_globals.php");

include("plugins/filter/search_highlight.php");


include("plugins/linking/link_css.php");
include("plugins/linking/linkexcerpts.php");

#-- security
include("plugins/auth/auth_perm_ring.php");
include("plugins/auth/userdb_array.php");

#-- own plugins, modified from original ewiki source
include("plugins/auth/dbforms_auth_method_form.php");
include("plugins/auth/dbforms_userdb_userregistry.php");

include("plugins/admin/control.php");

$_SERVER["HTTP_ACCEPT_LANGUAGE"] = "en";
         

#-- load ewiki 'lib'
include("ewiki.php");

error_reporting(E_NONE);



# admin account 
$ewiki_auth_user_array = array(
   "admin"  => array("jdbforms", $RING_LEVEL=0)
);

$passwords = array(
   "admin" => "jdbforms"
 );
?>
