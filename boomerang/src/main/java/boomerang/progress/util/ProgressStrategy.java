package boomerang.progress.util;

import boomerang.member.domain.Member;
import boomerang.progress.domain.*;


public interface ProgressStrategy {
    Progress makeProgress(Member member);
}
