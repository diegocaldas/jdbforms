<?php

  # this is the configuration for the ewiki page and database tools
  #     (which may need to be distinct from your main ewiki config)
  #


  #-- tools/ are run standalone?
  if (!function_exists("ewiki_database")) {


     #-- open db connection, load 'lib'
     include("../wiki_config.php");

     #-- simplest authentication:
     include("../wiki_auth.php");



  }


  #-- we now seem to run from inside ewiki (via the StaticPages plugin e.g.)
  else {

     #-- this terminates ewiki from within the spages plugin
     if (!EWIKI_PROTECTED_MODE || !ewiki_auth($id, $data, $action, 0, 2) || ($ewiki_ring>0) || !isset($ewiki_ring)) {
        die("Only the administrator can use this function.");
     }

  }



?>