<?php
 
#-- change timezone to GMT
putenv ("TZ=GMT"); 

#-- change dir to where this config script is located
chdir(dirname(__FILE__)."/ewiki");

#-- database connection/plugins
// MySQL support is built-in, we only open the connection
define("EWIKI_DB_TABLE_NAME", "ewiki");
define("EWIKI_SCRIPT", "http://jdbforms.sourceforge.net/wiki.php?page=");


#-- OPEN DATABASE for ewiki
#
if (function_exists("mysql_connect")) {
   $ok = @mysql_connect('mysql', 'jdbforms', 'db4712forms') && mysql_query("USE jdbforms");
}

#-- or use the flat file database backend as fallback
#if (!$ok) {
#   // define("EWIKI_DBFILES_DIRECTORY", "/tmp");
#   include("plugins/db/flat_files.php");
#}


#-- constants
define("EWIKI_NAME", 'DbFormsWiki');
define("EWIKI_PAGE_INDEX",       'WiKi');
define("EWIKI_PAGE_UPDATES",     'UpdatedPages');
define("EWIKI_PAGE_NEWEST",      'NewestPages');
define("EWIKI_PAGE_SEARCH",      'SearchPages');
define("EWIKI_PAGE_POWERSEARCH", 'PowerSearch');
define("EWIKI_SCRIPT", '?id=');
define("EWIKI_EMAILPROT_UNLOCK", 1);
define("EWIKI_AUTO_EDIT", 1);
define("EWIKI_EDIT_REDIRECT", 0);
define("EWIKI_HIT_COUNTING", 1);
define("EWIKI_RESOLVE_DNS", 1);
define("EWIKI_HTTP_HEADERS", 1);
define("EWIKI_ENGAGE_BINARY", 1);
define("EWIKI_CACHE_IMAGES", 1);
define("EWIKI_IMAGE_MAXSIZE", 65536);
define("EWIKI_LOGFILE", '/home/groups/j/jd/jdbforms/htdocs/log/ewiki.txt');
define("EWIKI_LOGLEVEL", '3');
define("EWIKI_TMP", '/tmp');
define("EWIKI_CASE_INSENSITIVE", 1);
define("EWIKI_PRINT_TITLE", 1);
define("EWIKI_CONTROL_LINE", 1);
define("EWIKI_LIST_LIMIT", 20);
define("EWIKI_EDIT_REDIRECT", 0);
define("EWIKI_NOTIFY_WITH_DIFF", 1);
define("EWIKI_NOTIFY_DIFF_PARAMS", " --ignore-case  --ignore-space-change");


#-- set a few configuration variables*
$ewiki_config["edit_thank_you"] = 0;
$ewiki_config["edit_box_size"] = '70x15';

#-- load plugins

include("plugins/lib/mime_magic.php");

include("plugins/email_protect.php");
#include("plugins/spages.php");
include("plugins/jump.php");
#include("plugins/notify.php");
include("../notify.php");
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
#include("plugins/page/wikiuserlogin.php");

include("plugins/action/diff.php");

include("plugins/aview/downloads.php");
include("plugins/module/downloads.php");

include("plugins/markup/css.php");
include("plugins/markup/naturallists.php");

include("fragments/strip_wonderful_slashes.php");
#include("fragments/strike_register_globals.php");

include("plugins/filter/search_highlight.php");


include("plugins/linking/link_css.php");
include("plugins/linking/linkexcerpts.php");


define("EWIKI_PROTECTED_MODE", 1);
#define("EWIKI_PROTECTED_MODE_HIDING", 1);
define("EWIKI_AUTO_LOGIN", 1);
include("plugins/auth/auth_perm_ring.php");
include("plugins/auth/userdb_array.php");

include("../wiki_auth_method_form.php");
include("../wiki_userdb_userregistry.php");

include("plugins/admin/control.php");

         

#-- load ewiki 'lib'
include("ewiki.php");

error_reporting(E_NONE);

# remove all languages
unset($ewiki_t["languages"]);
$ewiki_t["languages"][] = "en";


# admin account 
$ewiki_auth_user_array = array(
   "admin"  => array("jdbforms", $RING_LEVEL=0)
);

$passwords = array(
   "admin" => "jdbforms"
 );
?>