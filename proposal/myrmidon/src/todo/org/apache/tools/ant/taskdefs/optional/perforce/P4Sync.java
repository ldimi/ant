/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.Project;

/**
 * P4Sync - synchronise client space to a perforce depot view. The API allows
 * additional functionality of the "p4 sync" command (such as "p4 sync -f
 * //...#have" or other exotic invocations).</P> <b>Example Usage:</b>
 * <tableborder="1">
 *
 *   <th>
 *     Function
 *   </th>
 *
 *   <th>
 *     Command
 *   </th>
 *
 *   <tr>
 *
 *     <td>
 *       Sync to head using P4USER, P4PORT and P4CLIENT settings specified
 *     </td>
 *
 *     <td>
 *       &lt;P4Sync <br>
 *       P4view="//projects/foo/main/source/..." <br>
 *       P4User="fbloggs" <br>
 *       P4Port="km01:1666" <br>
 *       P4Client="fbloggsclient" /&gt;
 *     </td>
 *
 *   </tr>
 *
 *   <tr>
 *
 *     <td>
 *       Sync to head using P4USER, P4PORT and P4CLIENT settings defined in
 *       environment
 *     </td>
 *
 *     <td>
 *       &lt;P4Sync P4view="//projects/foo/main/source/..." /&gt;
 *     </td>
 *
 *   </tr>
 *
 *   <tr>
 *
 *     <td>
 *       Force a re-sync to head, refreshing all files
 *     </td>
 *
 *     <td>
 *       &lt;P4Sync force="yes" P4view="//projects/foo/main/source/..." /&gt;
 *
 *     </td>
 *
 *   </tr>
 *
 *   <tr>
 *
 *     <td>
 *       Sync to a label
 *     </td>
 *
 *     <td>
 *       &lt;P4Sync label="myPerforceLabel" /&gt;
 *     </td>
 *
 *   </tr>
 *
 * </table>
 * ToDo: Add decent label error handling for non-exsitant labels
 *
 * @author <A HREF="mailto:leslie.hughes@rubus.com">Les Hughes</A>
 */
public class P4Sync extends P4Base
{
    private String syncCmd = "";

    String label;

    public void setForce( String force )
        throws TaskException
    {
        if( force == null && !label.equals( "" ) )
            throw new TaskException( "P4Sync: If you want to force, set force to non-null string!" );
        P4CmdOpts = "-f";
    }

    public void setLabel( String label )
        throws TaskException
    {
        if( label == null && !label.equals( "" ) )
            throw new TaskException( "P4Sync: Labels cannot be Null or Empty" );

        this.label = label;

    }

    public void execute()
        throws TaskException
    {

        if( P4View != null )
        {
            syncCmd = P4View;
        }

        if( label != null && !label.equals( "" ) )
        {
            syncCmd = syncCmd + "@" + label;
        }

        log( "Execing sync " + P4CmdOpts + " " + syncCmd, Project.MSG_VERBOSE );

        execP4Command( "-s sync " + P4CmdOpts + " " + syncCmd, new SimpleP4OutputHandler( this ) );
    }
}
