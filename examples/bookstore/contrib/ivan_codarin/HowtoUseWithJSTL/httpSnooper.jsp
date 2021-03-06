<script language="JavaScript1.2">

var visible = false;

/**
 *  Toggle Visibility for the input layer
 *
 * @param layerName the value of the ID attribute of the DIV element
 */
function toggleVis(layerName)
{
  if (visible)
  {
    toggleVisibility(layerName,'hidden','hidden','hidden');
  }
  else
  {
    toggleVisibility(layerName,'show','visible','visible');
  }

  visible = !visible;
}


/**
 * Toggle Layer Visibility
 *
 * @author �Eddie Traversa (nirvana.media3.net)
 */
function toggleVisibility(id, NNtype, IEtype, WC3type) 
{
  if (document.getElementById) 
  {
     eval("document.getElementById(id).style.visibility = \"" + WC3type + "\"");
  } 
  else 
  {
    if (document.layers) 
    {
      document.layers[id].visibility = NNtype;
    } 
    else 
    {
      if (document.all) 
      {
        eval("document.all." + id + ".style.visibility = \"" + IEtype + "\"");
      }
    }
  }
}
</script>

<br><br>
<a href="javascript:void(0);" onClick="toggleVis('layer1');">HTTP debug</a>

<div id="layer1" style="visibility: hidden">
  <table width="100%" border="0">
  <tr>
    <td>
      <pre><%=org.dbforms.util.ServletUtil.dumpRequest(request) %></pre>
    </td>
  </tr>
  </table>
</div>
