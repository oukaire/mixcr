/*
 * Copyright (c) 2014-2018, Bolotin Dmitry, Chudakov Dmitry, Shugay Mikhail
 * (here and after addressed as Inventors)
 * All Rights Reserved
 *
 * Permission to use, copy, modify and distribute any part of this program for
 * educational, research and non-profit purposes, by non-profit institutions
 * only, without fee, and without a written agreement is hereby granted,
 * provided that the above copyright notice, this paragraph and the following
 * three paragraphs appear in all copies.
 *
 * Those desiring to incorporate this work into commercial products or use for
 * commercial purposes should contact MiLaboratory LLC, which owns exclusive
 * rights for distribution of this program for commercial purposes, using the
 * following email address: licensing@milaboratory.com.
 *
 * IN NO EVENT SHALL THE INVENTORS BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
 * SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
 * ARISING OUT OF THE USE OF THIS SOFTWARE, EVEN IF THE INVENTORS HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * THE SOFTWARE PROVIDED HEREIN IS ON AN "AS IS" BASIS, AND THE INVENTORS HAS
 * NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR
 * MODIFICATIONS. THE INVENTORS MAKES NO REPRESENTATIONS AND EXTENDS NO
 * WARRANTIES OF ANY KIND, EITHER IMPLIED OR EXPRESS, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A
 * PARTICULAR PURPOSE, OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY
 * PATENT, TRADEMARK OR OTHER RIGHTS.
 */
package com.milaboratory.mixcr.partialassembler;

import com.milaboratory.core.io.sequence.MultiRead;
import com.milaboratory.core.io.sequence.SequenceRead;
import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.io.sequence.SingleReadImpl;
import com.milaboratory.mixcr.basictypes.SequenceHistory;
import com.milaboratory.mixcr.basictypes.VDJCAlignments;

import java.util.List;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class VDJCMultiRead extends MultiRead {
    private final List<AlignedTarget> targets;

    public VDJCMultiRead(SingleRead... data) {
        super(data.clone());
        targets = null;
    }

    @SuppressWarnings("unchecked")
    public VDJCMultiRead(List<AlignedTarget> targets) {
        super(extractReads(targets));
        this.targets = targets;
        //this.expectedGeneTypes = new EnumSet[targets.size()];
        //for (int i = 0; i < expectedGeneTypes.length; i++)
        //    expectedGeneTypes[i] = targets.get(i).getExpectedGenes();
    }

    public static SingleReadImpl[] extractReads(List<AlignedTarget> targets) {
        final SingleReadImpl[] reads = new SingleReadImpl[targets.size()];
        for (int i = 0; i < reads.length; i++)
            reads[i] = new SingleReadImpl(-1, targets.get(i).getTarget(), "");
        return reads;
    }

    SequenceRead[] getOriginalReads() {
        if (targets == null)
            return null;
        VDJCAlignments[] result = new VDJCAlignments[targets.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = targets.get(i).getAlignments();
        return VDJCAlignments.mergeOriginalReads(result);
    }

    SequenceHistory[] getHistory() {
        SequenceHistory[] result = new SequenceHistory[numberOfReads()];
        for (int i = 0; i < result.length; i++)
            result[i] = targets == null
                    ? new SequenceHistory.RawSequence(getId(), (byte) i, false, getRead(i).getData().size())
                    : targets.get(i).getHistory();
        return result;
    }
}
