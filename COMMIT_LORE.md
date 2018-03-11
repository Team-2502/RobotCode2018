# Commit Lore
These are commits that have been tested and if things are not going very well at competition, should be reverted back to.
## Log
### 07df2152da0a1df74eaf20627491c9697078a9ba
All programmed autonomous paths work with 1 cube. Note: INVERSE PATHS HAVE NOT BEEN TESTED.

We did test left -> right scale, left -> left scale, center -> left switch, center -> right switch.

This is the last commit that has been tested on a field before Duluth.

### 9bfe2ae5b93b516d4acb209013f0835371b73579
[Northern Lights Quals 48](https://www.thebluealliance.com/match/2018mndu2_qm48)
- We undershot switch (even though previously had overshot it and no change?) ... need to fix
  - However previous overshoot MIGHT have been because it had accidentily been 10 ft forward instead of what we thought (9 ft).. about 20% chance.
