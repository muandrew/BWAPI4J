package org.openbw.bwapi4j.unit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openbw.bwapi4j.Position;
import org.openbw.bwapi4j.TilePosition;
import org.openbw.bwapi4j.test.BWDataProvider;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.util.Pair;

/**
 * Tests for refactoring {@link Building#getLastKnownDistance(Unit)} and similar methods.
 *
 * <p>"I have no idea if the methods were generating the correct values before. These tests are to
 * ensure the refactored methods produce the same values as before."-Adakite
 */
public class BuildingTest {
  private static final int[] LAST_KNOWN_DISTANCE_SAMPLES_BY_TILEPOSITION = {
    28, 27, 26, 26, 25, 25, 24, 23, 23, 22, 22, 21, 21, 21, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20,
        20, 20, 20, 20, 20, 20, 20, 21,
    27, 26, 26, 25, 24, 24, 23, 23, 22, 21, 21, 21, 20, 20, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19,
        19, 19, 19, 19, 19, 19, 19, 20,
    26, 26, 25, 24, 24, 23, 22, 22, 21, 21, 20, 20, 19, 19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18,
        18, 18, 18, 18, 18, 18, 18, 19,
    26, 25, 24, 24, 23, 22, 22, 21, 20, 20, 19, 19, 18, 18, 18, 17, 17, 17, 17, 17, 17, 17, 17, 17,
        17, 17, 17, 17, 17, 17, 18, 18,
    25, 24, 24, 23, 22, 21, 21, 20, 20, 19, 18, 18, 17, 17, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16,
        16, 16, 16, 16, 16, 16, 17, 17,
    25, 24, 23, 22, 21, 21, 20, 19, 19, 18, 18, 17, 17, 16, 16, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 16, 16,
    24, 23, 22, 22, 21, 20, 19, 19, 18, 17, 17, 16, 16, 15, 15, 14, 14, 14, 14, 14, 14, 14, 14, 14,
        14, 14, 14, 14, 14, 14, 15, 15,
    23, 23, 22, 21, 20, 19, 19, 18, 17, 17, 16, 15, 15, 14, 14, 13, 13, 13, 13, 13, 13, 13, 13, 13,
        13, 13, 13, 13, 13, 13, 14, 14,
    23, 22, 21, 20, 20, 19, 18, 17, 16, 16, 15, 15, 14, 13, 13, 13, 12, 12, 12, 12, 12, 12, 12, 12,
        12, 12, 12, 12, 12, 13, 13, 13,
    22, 21, 21, 20, 19, 18, 17, 17, 16, 15, 14, 14, 13, 13, 12, 12, 11, 11, 11, 11, 11, 11, 11, 11,
        11, 11, 11, 11, 11, 12, 12, 13,
    22, 21, 20, 19, 18, 18, 17, 16, 15, 14, 14, 13, 12, 12, 11, 11, 10, 10, 10, 10, 10, 10, 10, 10,
        10, 10, 10, 10, 10, 11, 11, 12,
    21, 21, 20, 19, 18, 17, 16, 15, 15, 14, 13, 12, 12, 11, 10, 10, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
        9, 9, 10, 10, 11,
    21, 20, 19, 18, 17, 17, 16, 15, 14, 13, 12, 12, 11, 10, 10, 9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
        8, 8, 9, 10, 10,
    21, 20, 19, 18, 17, 16, 15, 14, 13, 13, 12, 11, 10, 9, 9, 8, 8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
        8, 8, 9, 9,
    20, 19, 18, 18, 17, 16, 15, 14, 13, 12, 11, 10, 10, 9, 8, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
        7, 7, 8, 9,
    20, 19, 18, 17, 16, 15, 14, 13, 13, 12, 11, 10, 9, 8, 7, 7, 6, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
        6, 7, 7, 8,
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 8, 7, 6, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5,
        5, 6, 7, 8,
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 5, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4,
        5, 5, 6, 7,
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3,
        4, 5, 6, 7,
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 1, 1, 1, 1, 1, 2, 3,
        4, 5, 6, 7,
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 0, 0, 0, 0, 1, 2, 3,
        4, 5, 6, 7,
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 0, 0, 0, 0, 1, 2, 3,
        4, 5, 6, 7,
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 0, 0, 0, 0, 1, 2, 3,
        4, 5, 6, 7,
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 0, 0, 0, 0, 1, 2, 3,
        4, 5, 6, 7,
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 1, 1, 1, 1, 1, 2, 3,
        4, 5, 6, 7,
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3,
        4, 5, 6, 7,
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 5, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4,
        5, 5, 6, 7,
    20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 8, 7, 6, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5,
        5, 6, 7, 8,
    20, 19, 18, 17, 16, 15, 14, 13, 13, 12, 11, 10, 9, 8, 7, 7, 6, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
        6, 7, 7, 8,
    20, 19, 18, 18, 17, 16, 15, 14, 13, 12, 11, 10, 10, 9, 8, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
        7, 7, 8, 9,
    21, 20, 19, 18, 17, 16, 15, 14, 13, 13, 12, 11, 10, 9, 9, 8, 8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
        8, 8, 9, 9,
    21, 20, 19, 18, 17, 17, 16, 15, 14, 13, 12, 12, 11, 10, 10, 9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
        8, 8, 9, 10, 10
  };

  private static final double[] LAST_KNOWN_DISTANCE_SAMPLES_BY_POSITION = {
    904.0, 881.0, 859.0, 838.0, 817.0, 797.0, 778.0, 760.0, 743.0, 726.0, 711.0, 697.0, 684.0,
        672.0, 662.0, 653.0, 646.0, 640.0, 635.0, 633.0, 632.0, 632.0, 632.0, 632.0, 632.0, 633.0,
        635.0, 640.0, 646.0, 653.0, 662.0, 672.0,
    882.0, 859.0, 836.0, 814.0, 793.0, 772.0, 753.0, 734.0, 716.0, 699.0, 683.0, 668.0, 655.0,
        642.0, 632.0, 622.0, 615.0, 608.0, 604.0, 601.0, 600.0, 600.0, 600.0, 600.0, 600.0, 601.0,
        604.0, 608.0, 615.0, 622.0, 632.0, 642.0,
    860.0, 837.0, 813.0, 791.0, 769.0, 748.0, 727.0, 708.0, 689.0, 671.0, 655.0, 640.0, 625.0,
        613.0, 601.0, 592.0, 583.0, 577.0, 572.0, 569.0, 568.0, 568.0, 568.0, 568.0, 568.0, 569.0,
        572.0, 577.0, 583.0, 592.0, 601.0, 613.0,
    840.0, 815.0, 791.0, 768.0, 746.0, 724.0, 703.0, 682.0, 663.0, 645.0, 627.0, 611.0, 597.0,
        583.0, 571.0, 561.0, 552.0, 545.0, 540.0, 537.0, 536.0, 536.0, 536.0, 536.0, 536.0, 537.0,
        540.0, 545.0, 552.0, 561.0, 571.0, 583.0,
    820.0, 795.0, 770.0, 746.0, 723.0, 700.0, 679.0, 657.0, 637.0, 618.0, 600.0, 583.0, 568.0,
        554.0, 541.0, 530.0, 521.0, 514.0, 508.0, 505.0, 504.0, 504.0, 504.0, 504.0, 504.0, 505.0,
        508.0, 514.0, 521.0, 530.0, 541.0, 554.0,
    800.0, 775.0, 750.0, 725.0, 701.0, 678.0, 655.0, 633.0, 612.0, 593.0, 574.0, 556.0, 540.0,
        525.0, 512.0, 500.0, 490.0, 483.0, 477.0, 473.0, 472.0, 472.0, 472.0, 472.0, 472.0, 473.0,
        477.0, 483.0, 490.0, 500.0, 512.0, 525.0,
    782.0, 756.0, 730.0, 705.0, 680.0, 656.0, 632.0, 610.0, 588.0, 567.0, 548.0, 529.0, 512.0,
        496.0, 482.0, 470.0, 460.0, 451.0, 445.0, 441.0, 440.0, 440.0, 440.0, 440.0, 440.0, 441.0,
        445.0, 451.0, 460.0, 470.0, 482.0, 496.0,
    764.0, 738.0, 711.0, 685.0, 660.0, 635.0, 611.0, 587.0, 565.0, 543.0, 522.0, 503.0, 485.0,
        468.0, 453.0, 440.0, 429.0, 420.0, 414.0, 409.0, 408.0, 408.0, 408.0, 408.0, 408.0, 409.0,
        414.0, 420.0, 429.0, 440.0, 453.0, 468.0,
    748.0, 720.0, 693.0, 667.0, 640.0, 615.0, 590.0, 565.0, 542.0, 519.0, 498.0, 477.0, 458.0,
        441.0, 425.0, 411.0, 399.0, 389.0, 382.0, 378.0, 376.0, 376.0, 376.0, 376.0, 376.0, 378.0,
        382.0, 389.0, 399.0, 411.0, 425.0, 441.0,
    732.0, 704.0, 676.0, 649.0, 622.0, 596.0, 570.0, 545.0, 520.0, 497.0, 474.0, 453.0, 433.0,
        414.0, 397.0, 382.0, 369.0, 359.0, 351.0, 346.0, 344.0, 344.0, 344.0, 344.0, 344.0, 346.0,
        351.0, 359.0, 369.0, 382.0, 397.0, 414.0,
    718.0, 689.0, 661.0, 633.0, 605.0, 578.0, 551.0, 525.0, 500.0, 475.0, 451.0, 429.0, 408.0,
        388.0, 370.0, 353.0, 339.0, 328.0, 319.0, 314.0, 312.0, 312.0, 312.0, 312.0, 312.0, 314.0,
        319.0, 328.0, 339.0, 353.0, 370.0, 388.0,
    704.0, 675.0, 646.0, 618.0, 589.0, 561.0, 534.0, 507.0, 480.0, 455.0, 430.0, 406.0, 384.0,
        362.0, 343.0, 326.0, 310.0, 298.0, 288.0, 282.0, 280.0, 280.0, 280.0, 280.0, 280.0, 282.0,
        288.0, 298.0, 310.0, 326.0, 343.0, 362.0,
    692.0, 663.0, 633.0, 604.0, 575.0, 546.0, 518.0, 490.0, 463.0, 436.0, 410.0, 385.0, 361.0,
        338.0, 317.0, 298.0, 282.0, 268.0, 257.0, 251.0, 248.0, 248.0, 248.0, 248.0, 248.0, 251.0,
        257.0, 268.0, 282.0, 298.0, 317.0, 338.0,
    682.0, 651.0, 621.0, 591.0, 562.0, 532.0, 503.0, 474.0, 446.0, 418.0, 391.0, 365.0, 340.0,
        316.0, 293.0, 273.0, 254.0, 239.0, 227.0, 219.0, 216.0, 216.0, 216.0, 216.0, 216.0, 219.0,
        227.0, 239.0, 254.0, 273.0, 293.0, 316.0,
    672.0, 641.0, 611.0, 580.0, 550.0, 520.0, 490.0, 461.0, 432.0, 403.0, 375.0, 347.0, 320.0,
        295.0, 271.0, 248.0, 228.0, 210.0, 197.0, 188.0, 184.0, 184.0, 184.0, 184.0, 184.0, 188.0,
        197.0, 210.0, 228.0, 248.0, 271.0, 295.0,
    664.0, 633.0, 602.0, 571.0, 540.0, 510.0, 479.0, 449.0, 419.0, 389.0, 360.0, 331.0, 303.0,
        276.0, 250.0, 225.0, 203.0, 183.0, 167.0, 156.0, 152.0, 152.0, 152.0, 152.0, 152.0, 156.0,
        167.0, 183.0, 203.0, 225.0, 250.0, 276.0,
    658.0, 626.0, 595.0, 563.0, 532.0, 501.0, 470.0, 439.0, 409.0, 378.0, 348.0, 318.0, 289.0,
        260.0, 232.0, 205.0, 180.0, 158.0, 139.0, 126.0, 120.0, 120.0, 120.0, 120.0, 120.0, 126.0,
        139.0, 158.0, 180.0, 205.0, 232.0, 260.0,
    652.0, 621.0, 589.0, 557.0, 526.0, 494.0, 463.0, 432.0, 400.0, 369.0, 338.0, 307.0, 277.0,
        247.0, 217.0, 188.0, 161.0, 135.0, 113.0, 96.0, 88.0, 88.0, 88.0, 88.0, 88.0, 96.0, 113.0,
        135.0, 161.0, 188.0, 217.0, 247.0,
    649.0, 617.0, 585.0, 553.0, 522.0, 490.0, 458.0, 426.0, 394.0, 363.0, 331.0, 300.0, 268.0,
        237.0, 206.0, 176.0, 146.0, 117.0, 90.0, 68.0, 56.0, 56.0, 56.0, 56.0, 56.0, 68.0, 90.0,
        117.0, 146.0, 176.0, 206.0, 237.0,
    647.0, 615.0, 583.0, 551.0, 519.0, 487.0, 455.0, 423.0, 391.0, 359.0, 327.0, 295.0, 264.0,
        232.0, 200.0, 168.0, 137.0, 105.0, 74.0, 45.0, 25.0, 24.0, 24.0, 24.0, 25.0, 45.0, 74.0,
        105.0, 137.0, 168.0, 200.0, 232.0,
    647.0, 615.0, 583.0, 551.0, 519.0, 487.0, 455.0, 423.0, 391.0, 359.0, 327.0, 295.0, 263.0,
        231.0, 199.0, 167.0, 135.0, 103.0, 71.0, 39.0, 7.0, 0.0, 0.0, 0.0, 7.0, 39.0, 71.0, 103.0,
        135.0, 167.0, 199.0, 231.0,
    647.0, 615.0, 583.0, 551.0, 519.0, 487.0, 455.0, 423.0, 391.0, 359.0, 327.0, 295.0, 263.0,
        231.0, 199.0, 167.0, 135.0, 103.0, 71.0, 39.0, 7.0, 0.0, 0.0, 0.0, 7.0, 39.0, 71.0, 103.0,
        135.0, 167.0, 199.0, 231.0,
    647.0, 615.0, 583.0, 551.0, 519.0, 487.0, 455.0, 423.0, 391.0, 359.0, 327.0, 295.0, 263.0,
        231.0, 199.0, 167.0, 135.0, 103.0, 71.0, 39.0, 7.0, 0.0, 0.0, 0.0, 7.0, 39.0, 71.0, 103.0,
        135.0, 167.0, 199.0, 231.0,
    647.0, 615.0, 583.0, 551.0, 519.0, 487.0, 455.0, 423.0, 391.0, 359.0, 327.0, 295.0, 264.0,
        232.0, 200.0, 168.0, 137.0, 105.0, 74.0, 45.0, 25.0, 24.0, 24.0, 24.0, 25.0, 45.0, 74.0,
        105.0, 137.0, 168.0, 200.0, 232.0,
    649.0, 617.0, 585.0, 553.0, 522.0, 490.0, 458.0, 426.0, 394.0, 363.0, 331.0, 300.0, 268.0,
        237.0, 206.0, 176.0, 146.0, 117.0, 90.0, 68.0, 56.0, 56.0, 56.0, 56.0, 56.0, 68.0, 90.0,
        117.0, 146.0, 176.0, 206.0, 237.0,
    652.0, 621.0, 589.0, 557.0, 526.0, 494.0, 463.0, 432.0, 400.0, 369.0, 338.0, 307.0, 277.0,
        247.0, 217.0, 188.0, 161.0, 135.0, 113.0, 96.0, 88.0, 88.0, 88.0, 88.0, 88.0, 96.0, 113.0,
        135.0, 161.0, 188.0, 217.0, 247.0,
    658.0, 626.0, 595.0, 563.0, 532.0, 501.0, 470.0, 439.0, 409.0, 378.0, 348.0, 318.0, 289.0,
        260.0, 232.0, 205.0, 180.0, 158.0, 139.0, 126.0, 120.0, 120.0, 120.0, 120.0, 120.0, 126.0,
        139.0, 158.0, 180.0, 205.0, 232.0, 260.0,
    664.0, 633.0, 602.0, 571.0, 540.0, 510.0, 479.0, 449.0, 419.0, 389.0, 360.0, 331.0, 303.0,
        276.0, 250.0, 225.0, 203.0, 183.0, 167.0, 156.0, 152.0, 152.0, 152.0, 152.0, 152.0, 156.0,
        167.0, 183.0, 203.0, 225.0, 250.0, 276.0,
    672.0, 641.0, 611.0, 580.0, 550.0, 520.0, 490.0, 461.0, 432.0, 403.0, 375.0, 347.0, 320.0,
        295.0, 271.0, 248.0, 228.0, 210.0, 197.0, 188.0, 184.0, 184.0, 184.0, 184.0, 184.0, 188.0,
        197.0, 210.0, 228.0, 248.0, 271.0, 295.0,
    682.0, 651.0, 621.0, 591.0, 562.0, 532.0, 503.0, 474.0, 446.0, 418.0, 391.0, 365.0, 340.0,
        316.0, 293.0, 273.0, 254.0, 239.0, 227.0, 219.0, 216.0, 216.0, 216.0, 216.0, 216.0, 219.0,
        227.0, 239.0, 254.0, 273.0, 293.0, 316.0,
    692.0, 663.0, 633.0, 604.0, 575.0, 546.0, 518.0, 490.0, 463.0, 436.0, 410.0, 385.0, 361.0,
        338.0, 317.0, 298.0, 282.0, 268.0, 257.0, 251.0, 248.0, 248.0, 248.0, 248.0, 248.0, 251.0,
        257.0, 268.0, 282.0, 298.0, 317.0, 338.0,
    704.0, 675.0, 646.0, 618.0, 589.0, 561.0, 534.0, 507.0, 480.0, 455.0, 430.0, 406.0, 384.0,
        362.0, 343.0, 326.0, 310.0, 298.0, 288.0, 282.0, 280.0, 280.0, 280.0, 280.0, 280.0, 282.0,
        288.0, 298.0, 310.0, 326.0, 343.0, 362.0
  };

  private static final double[] LAST_KNOWN_DISTANCE_SAMPLES_BY_UNIT1 = {
    819.0, 818.0, 817.0, 816.0, 815.0, 814.0, 814.0, 813.0, 812.0, 811.0, 810.0, 809.0, 809.0,
        809.0, 808.0, 808.0, 807.0, 807.0, 807.0, 806.0, 806.0, 806.0, 805.0, 805.0, 804.0, 804.0,
        804.0, 803.0, 803.0, 803.0, 802.0, 802.0,
    819.0, 818.0, 817.0, 816.0, 815.0, 814.0, 814.0, 813.0, 812.0, 811.0, 810.0, 809.0, 808.0,
        808.0, 807.0, 807.0, 806.0, 806.0, 806.0, 805.0, 805.0, 805.0, 804.0, 804.0, 803.0, 803.0,
        803.0, 802.0, 802.0, 802.0, 801.0, 801.0,
    819.0, 818.0, 817.0, 816.0, 815.0, 814.0, 814.0, 813.0, 812.0, 811.0, 810.0, 809.0, 808.0,
        807.0, 806.0, 806.0, 805.0, 805.0, 805.0, 804.0, 804.0, 804.0, 803.0, 803.0, 802.0, 802.0,
        802.0, 801.0, 801.0, 801.0, 800.0, 800.0,
    818.0, 817.0, 816.0, 815.0, 814.0, 813.0, 813.0, 812.0, 811.0, 810.0, 809.0, 808.0, 807.0,
        806.0, 805.0, 805.0, 804.0, 804.0, 804.0, 803.0, 803.0, 803.0, 802.0, 802.0, 801.0, 801.0,
        801.0, 800.0, 800.0, 800.0, 799.0, 799.0,
    818.0, 817.0, 816.0, 815.0, 814.0, 813.0, 813.0, 812.0, 811.0, 810.0, 809.0, 808.0, 807.0,
        806.0, 805.0, 804.0, 803.0, 803.0, 803.0, 802.0, 802.0, 802.0, 801.0, 801.0, 800.0, 800.0,
        800.0, 799.0, 799.0, 799.0, 798.0, 798.0,
    817.0, 816.0, 815.0, 814.0, 813.0, 812.0, 812.0, 811.0, 810.0, 809.0, 808.0, 807.0, 806.0,
        805.0, 804.0, 803.0, 802.0, 802.0, 802.0, 801.0, 801.0, 801.0, 800.0, 800.0, 799.0, 799.0,
        799.0, 798.0, 798.0, 798.0, 797.0, 797.0,
    817.0, 816.0, 815.0, 814.0, 813.0, 812.0, 812.0, 811.0, 810.0, 809.0, 808.0, 807.0, 806.0,
        805.0, 804.0, 803.0, 802.0, 801.0, 801.0, 800.0, 800.0, 800.0, 799.0, 799.0, 798.0, 798.0,
        798.0, 797.0, 797.0, 797.0, 796.0, 796.0,
    817.0, 816.0, 815.0, 814.0, 813.0, 812.0, 812.0, 811.0, 810.0, 809.0, 808.0, 807.0, 806.0,
        805.0, 804.0, 803.0, 802.0, 801.0, 800.0, 799.0, 799.0, 799.0, 798.0, 798.0, 797.0, 797.0,
        797.0, 796.0, 796.0, 796.0, 795.0, 795.0,
    816.0, 815.0, 814.0, 813.0, 812.0, 811.0, 811.0, 810.0, 809.0, 808.0, 807.0, 806.0, 805.0,
        804.0, 803.0, 802.0, 801.0, 800.0, 799.0, 798.0, 798.0, 798.0, 797.0, 797.0, 796.0, 796.0,
        796.0, 795.0, 795.0, 795.0, 794.0, 794.0,
    816.0, 815.0, 814.0, 813.0, 812.0, 811.0, 811.0, 810.0, 809.0, 808.0, 807.0, 806.0, 805.0,
        804.0, 803.0, 802.0, 801.0, 800.0, 799.0, 798.0, 797.0, 797.0, 796.0, 796.0, 795.0, 795.0,
        795.0, 794.0, 794.0, 794.0, 793.0, 793.0,
    816.0, 815.0, 814.0, 813.0, 812.0, 811.0, 811.0, 810.0, 809.0, 808.0, 807.0, 806.0, 805.0,
        804.0, 803.0, 802.0, 801.0, 800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 795.0, 794.0, 794.0,
        794.0, 793.0, 793.0, 793.0, 792.0, 792.0,
    815.0, 814.0, 813.0, 812.0, 811.0, 810.0, 810.0, 809.0, 808.0, 807.0, 806.0, 805.0, 804.0,
        803.0, 802.0, 801.0, 800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 795.0, 795.0, 794.0, 794.0,
        794.0, 793.0, 793.0, 793.0, 792.0, 792.0,
    815.0, 814.0, 813.0, 812.0, 811.0, 810.0, 810.0, 809.0, 808.0, 807.0, 806.0, 805.0, 804.0,
        803.0, 802.0, 801.0, 800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 795.0, 794.0, 793.0, 793.0,
        793.0, 792.0, 792.0, 792.0, 791.0, 791.0,
    814.0, 813.0, 812.0, 811.0, 810.0, 809.0, 809.0, 808.0, 807.0, 806.0, 805.0, 804.0, 803.0,
        802.0, 801.0, 800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 794.0, 793.0, 792.0, 792.0,
        792.0, 791.0, 791.0, 791.0, 790.0, 790.0,
    814.0, 813.0, 812.0, 811.0, 810.0, 809.0, 809.0, 808.0, 807.0, 806.0, 805.0, 804.0, 803.0,
        802.0, 801.0, 800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 794.0, 793.0, 792.0, 791.0,
        791.0, 790.0, 790.0, 790.0, 789.0, 789.0,
    814.0, 813.0, 812.0, 811.0, 810.0, 809.0, 809.0, 808.0, 807.0, 806.0, 805.0, 804.0, 803.0,
        802.0, 801.0, 800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 794.0, 793.0, 792.0, 791.0,
        790.0, 789.0, 789.0, 789.0, 788.0, 788.0,
    813.0, 812.0, 811.0, 810.0, 809.0, 808.0, 808.0, 807.0, 806.0, 805.0, 804.0, 803.0, 802.0,
        801.0, 800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 793.0, 793.0, 792.0, 791.0, 790.0,
        789.0, 788.0, 788.0, 788.0, 787.0, 787.0,
    813.0, 812.0, 811.0, 810.0, 809.0, 808.0, 808.0, 807.0, 806.0, 805.0, 804.0, 803.0, 802.0,
        801.0, 800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 793.0, 793.0, 792.0, 791.0, 790.0,
        789.0, 788.0, 787.0, 787.0, 786.0, 786.0,
    813.0, 812.0, 811.0, 810.0, 809.0, 808.0, 808.0, 807.0, 806.0, 805.0, 804.0, 803.0, 802.0,
        801.0, 800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 793.0, 793.0, 792.0, 791.0, 790.0,
        789.0, 788.0, 787.0, 786.0, 785.0, 785.0,
    812.0, 811.0, 810.0, 809.0, 808.0, 807.0, 807.0, 806.0, 805.0, 804.0, 803.0, 802.0, 801.0,
        800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 793.0, 792.0, 792.0, 791.0, 790.0, 789.0,
        788.0, 787.0, 786.0, 785.0, 784.0, 784.0,
    812.0, 811.0, 810.0, 809.0, 808.0, 807.0, 807.0, 806.0, 805.0, 804.0, 803.0, 802.0, 801.0,
        800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 793.0, 792.0, 792.0, 791.0, 790.0, 789.0,
        788.0, 787.0, 786.0, 785.0, 784.0, 783.0,
    810.0, 809.0, 808.0, 807.0, 806.0, 805.0, 805.0, 804.0, 803.0, 802.0, 801.0, 800.0, 799.0,
        798.0, 797.0, 796.0, 795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 790.0, 789.0, 788.0, 787.0,
        786.0, 785.0, 784.0, 783.0, 782.0, 781.0,
    810.0, 809.0, 808.0, 807.0, 806.0, 805.0, 805.0, 804.0, 803.0, 802.0, 801.0, 800.0, 799.0,
        798.0, 797.0, 796.0, 795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 790.0, 789.0, 788.0, 787.0,
        786.0, 785.0, 784.0, 783.0, 782.0, 781.0,
    810.0, 809.0, 808.0, 807.0, 806.0, 805.0, 805.0, 804.0, 803.0, 802.0, 801.0, 800.0, 799.0,
        798.0, 797.0, 796.0, 795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 790.0, 789.0, 788.0, 787.0,
        786.0, 785.0, 784.0, 783.0, 782.0, 781.0,
    809.0, 808.0, 807.0, 806.0, 805.0, 804.0, 804.0, 803.0, 802.0, 801.0, 800.0, 799.0, 798.0,
        797.0, 796.0, 795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 789.0, 788.0, 787.0, 786.0,
        785.0, 784.0, 783.0, 782.0, 781.0, 780.0,
    809.0, 808.0, 807.0, 806.0, 805.0, 804.0, 804.0, 803.0, 802.0, 801.0, 800.0, 799.0, 798.0,
        797.0, 796.0, 795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 789.0, 788.0, 787.0, 786.0,
        785.0, 784.0, 783.0, 782.0, 781.0, 780.0,
    809.0, 808.0, 807.0, 806.0, 805.0, 804.0, 804.0, 803.0, 802.0, 801.0, 800.0, 799.0, 798.0,
        797.0, 796.0, 795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 789.0, 788.0, 787.0, 786.0,
        785.0, 784.0, 783.0, 782.0, 781.0, 780.0,
    808.0, 807.0, 806.0, 805.0, 804.0, 803.0, 803.0, 802.0, 801.0, 800.0, 799.0, 798.0, 797.0,
        796.0, 795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 788.0, 788.0, 787.0, 786.0, 785.0,
        784.0, 783.0, 782.0, 781.0, 780.0, 779.0,
    808.0, 807.0, 806.0, 805.0, 804.0, 803.0, 803.0, 802.0, 801.0, 800.0, 799.0, 798.0, 797.0,
        796.0, 795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 788.0, 788.0, 787.0, 786.0, 785.0,
        784.0, 783.0, 782.0, 781.0, 780.0, 779.0,
    807.0, 806.0, 805.0, 804.0, 803.0, 802.0, 802.0, 801.0, 800.0, 799.0, 798.0, 797.0, 796.0,
        795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 788.0, 787.0, 787.0, 786.0, 785.0, 784.0,
        783.0, 782.0, 781.0, 780.0, 779.0, 778.0,
    807.0, 806.0, 805.0, 804.0, 803.0, 802.0, 802.0, 801.0, 800.0, 799.0, 798.0, 797.0, 796.0,
        795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 788.0, 787.0, 787.0, 786.0, 785.0, 784.0,
        783.0, 782.0, 781.0, 780.0, 779.0, 778.0,
    807.0, 806.0, 805.0, 804.0, 803.0, 802.0, 802.0, 801.0, 800.0, 799.0, 798.0, 797.0, 796.0,
        795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 788.0, 787.0, 787.0, 786.0, 785.0, 784.0,
        783.0, 782.0, 781.0, 780.0, 779.0, 778.0,
  };

  private static final double[] LAST_KNOWN_DISTANCE_SAMPLES_BY_UNIT2 = {
    806.0, 806.0, 805.0, 805.0, 805.0, 804.0, 804.0, 804.0, 803.0, 803.0, 802.0, 802.0, 802.0,
        801.0, 801.0, 801.0, 800.0, 800.0, 798.0, 798.0, 798.0, 797.0, 797.0, 797.0, 796.0, 796.0,
        795.0, 795.0, 795.0, 794.0, 794.0, 794.0,
    805.0, 805.0, 804.0, 804.0, 804.0, 803.0, 803.0, 803.0, 802.0, 802.0, 801.0, 801.0, 801.0,
        800.0, 800.0, 800.0, 799.0, 799.0, 797.0, 797.0, 797.0, 796.0, 796.0, 796.0, 795.0, 795.0,
        794.0, 794.0, 794.0, 793.0, 793.0, 793.0,
    805.0, 804.0, 803.0, 803.0, 803.0, 802.0, 802.0, 802.0, 801.0, 801.0, 800.0, 800.0, 800.0,
        799.0, 799.0, 799.0, 798.0, 798.0, 796.0, 796.0, 796.0, 795.0, 795.0, 795.0, 794.0, 794.0,
        793.0, 793.0, 793.0, 792.0, 792.0, 792.0,
    804.0, 803.0, 802.0, 802.0, 802.0, 801.0, 801.0, 801.0, 800.0, 800.0, 799.0, 799.0, 799.0,
        798.0, 798.0, 798.0, 797.0, 797.0, 795.0, 795.0, 795.0, 794.0, 794.0, 794.0, 793.0, 793.0,
        792.0, 792.0, 792.0, 791.0, 791.0, 791.0,
    804.0, 803.0, 802.0, 801.0, 801.0, 800.0, 800.0, 800.0, 799.0, 799.0, 798.0, 798.0, 798.0,
        797.0, 797.0, 797.0, 796.0, 796.0, 794.0, 794.0, 794.0, 793.0, 793.0, 793.0, 792.0, 792.0,
        791.0, 791.0, 791.0, 790.0, 790.0, 790.0,
    804.0, 803.0, 802.0, 801.0, 800.0, 799.0, 799.0, 799.0, 798.0, 798.0, 797.0, 797.0, 797.0,
        796.0, 796.0, 796.0, 795.0, 795.0, 793.0, 793.0, 793.0, 792.0, 792.0, 792.0, 791.0, 791.0,
        790.0, 790.0, 790.0, 789.0, 789.0, 789.0,
    803.0, 802.0, 801.0, 800.0, 799.0, 798.0, 798.0, 798.0, 797.0, 797.0, 796.0, 796.0, 796.0,
        795.0, 795.0, 795.0, 794.0, 794.0, 792.0, 792.0, 792.0, 791.0, 791.0, 791.0, 790.0, 790.0,
        789.0, 789.0, 789.0, 788.0, 788.0, 788.0,
    803.0, 802.0, 801.0, 800.0, 799.0, 798.0, 797.0, 797.0, 796.0, 796.0, 795.0, 795.0, 795.0,
        794.0, 794.0, 794.0, 793.0, 793.0, 791.0, 791.0, 791.0, 790.0, 790.0, 790.0, 789.0, 789.0,
        788.0, 788.0, 788.0, 787.0, 787.0, 787.0,
    803.0, 802.0, 801.0, 800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 795.0, 794.0, 794.0, 794.0,
        793.0, 793.0, 793.0, 792.0, 792.0, 790.0, 790.0, 790.0, 789.0, 789.0, 789.0, 788.0, 788.0,
        787.0, 787.0, 787.0, 786.0, 786.0, 786.0,
    802.0, 801.0, 800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 795.0, 795.0, 794.0, 794.0, 794.0,
        793.0, 793.0, 793.0, 792.0, 792.0, 790.0, 790.0, 790.0, 789.0, 789.0, 789.0, 788.0, 788.0,
        787.0, 787.0, 787.0, 786.0, 786.0, 786.0,
    802.0, 801.0, 800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 795.0, 794.0, 793.0, 793.0, 793.0,
        792.0, 792.0, 792.0, 791.0, 791.0, 789.0, 789.0, 789.0, 788.0, 788.0, 788.0, 787.0, 787.0,
        786.0, 786.0, 786.0, 785.0, 785.0, 785.0,
    801.0, 800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 794.0, 793.0, 792.0, 792.0, 792.0,
        791.0, 791.0, 791.0, 790.0, 790.0, 788.0, 788.0, 788.0, 787.0, 787.0, 787.0, 786.0, 786.0,
        785.0, 785.0, 785.0, 784.0, 784.0, 784.0,
    801.0, 800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 794.0, 793.0, 792.0, 791.0, 791.0,
        790.0, 790.0, 790.0, 789.0, 789.0, 787.0, 787.0, 787.0, 786.0, 786.0, 786.0, 785.0, 785.0,
        784.0, 784.0, 784.0, 783.0, 783.0, 783.0,
    801.0, 800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 794.0, 793.0, 792.0, 791.0, 790.0,
        789.0, 789.0, 789.0, 788.0, 788.0, 786.0, 786.0, 786.0, 785.0, 785.0, 785.0, 784.0, 784.0,
        783.0, 783.0, 783.0, 782.0, 782.0, 782.0,
    800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 793.0, 793.0, 792.0, 791.0, 790.0, 789.0,
        788.0, 788.0, 788.0, 787.0, 787.0, 785.0, 785.0, 785.0, 784.0, 784.0, 784.0, 783.0, 783.0,
        782.0, 782.0, 782.0, 781.0, 781.0, 781.0,
    800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 793.0, 793.0, 792.0, 791.0, 790.0, 789.0,
        788.0, 787.0, 787.0, 786.0, 786.0, 784.0, 784.0, 784.0, 783.0, 783.0, 783.0, 782.0, 782.0,
        781.0, 781.0, 781.0, 780.0, 780.0, 780.0,
    800.0, 799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 793.0, 793.0, 792.0, 791.0, 790.0, 789.0,
        788.0, 787.0, 786.0, 785.0, 785.0, 783.0, 783.0, 783.0, 782.0, 782.0, 782.0, 781.0, 781.0,
        780.0, 780.0, 780.0, 779.0, 779.0, 779.0,
    799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 793.0, 792.0, 792.0, 791.0, 790.0, 789.0, 788.0,
        787.0, 786.0, 785.0, 784.0, 784.0, 782.0, 782.0, 782.0, 781.0, 781.0, 781.0, 780.0, 780.0,
        779.0, 779.0, 779.0, 778.0, 778.0, 778.0,
    799.0, 798.0, 797.0, 796.0, 795.0, 794.0, 793.0, 792.0, 792.0, 791.0, 790.0, 789.0, 788.0,
        787.0, 786.0, 785.0, 784.0, 783.0, 781.0, 781.0, 781.0, 780.0, 780.0, 780.0, 779.0, 779.0,
        778.0, 778.0, 778.0, 777.0, 777.0, 777.0,
    797.0, 796.0, 795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 790.0, 789.0, 788.0, 787.0, 786.0,
        785.0, 784.0, 783.0, 782.0, 781.0, 780.0, 780.0, 780.0, 779.0, 779.0, 779.0, 778.0, 778.0,
        777.0, 777.0, 777.0, 776.0, 776.0, 776.0,
    797.0, 796.0, 795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 790.0, 789.0, 788.0, 787.0, 786.0,
        785.0, 784.0, 783.0, 782.0, 781.0, 780.0, 779.0, 779.0, 778.0, 778.0, 778.0, 777.0, 777.0,
        776.0, 776.0, 776.0, 775.0, 775.0, 775.0,
    797.0, 796.0, 795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 790.0, 789.0, 788.0, 787.0, 786.0,
        785.0, 784.0, 783.0, 782.0, 781.0, 780.0, 779.0, 778.0, 777.0, 777.0, 777.0, 776.0, 776.0,
        775.0, 775.0, 775.0, 774.0, 774.0, 774.0,
    796.0, 795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 789.0, 788.0, 787.0, 786.0, 785.0,
        784.0, 783.0, 782.0, 781.0, 780.0, 779.0, 778.0, 777.0, 776.0, 776.0, 776.0, 775.0, 775.0,
        774.0, 774.0, 774.0, 773.0, 773.0, 773.0,
    796.0, 795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 789.0, 788.0, 787.0, 786.0, 785.0,
        784.0, 783.0, 782.0, 781.0, 780.0, 779.0, 778.0, 777.0, 776.0, 775.0, 775.0, 774.0, 774.0,
        773.0, 773.0, 773.0, 772.0, 772.0, 772.0,
    796.0, 795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 789.0, 788.0, 787.0, 786.0, 785.0,
        784.0, 783.0, 782.0, 781.0, 780.0, 779.0, 778.0, 777.0, 776.0, 775.0, 774.0, 773.0, 773.0,
        772.0, 772.0, 772.0, 771.0, 771.0, 771.0,
    795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 788.0, 788.0, 787.0, 786.0, 785.0, 784.0,
        783.0, 782.0, 781.0, 780.0, 779.0, 778.0, 777.0, 776.0, 775.0, 774.0, 773.0, 773.0, 773.0,
        772.0, 772.0, 772.0, 771.0, 771.0, 771.0,
    795.0, 794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 788.0, 788.0, 787.0, 786.0, 785.0, 784.0,
        783.0, 782.0, 781.0, 780.0, 779.0, 778.0, 777.0, 776.0, 775.0, 774.0, 773.0, 773.0, 772.0,
        771.0, 771.0, 771.0, 770.0, 770.0, 770.0,
    794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 788.0, 787.0, 787.0, 786.0, 785.0, 784.0, 783.0,
        782.0, 781.0, 780.0, 779.0, 778.0, 777.0, 776.0, 775.0, 774.0, 773.0, 772.0, 772.0, 771.0,
        770.0, 770.0, 770.0, 769.0, 769.0, 769.0,
    794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 788.0, 787.0, 787.0, 786.0, 785.0, 784.0, 783.0,
        782.0, 781.0, 780.0, 779.0, 778.0, 777.0, 776.0, 775.0, 774.0, 773.0, 772.0, 772.0, 771.0,
        770.0, 769.0, 769.0, 768.0, 768.0, 768.0,
    794.0, 793.0, 792.0, 791.0, 790.0, 789.0, 788.0, 787.0, 787.0, 786.0, 785.0, 784.0, 783.0,
        782.0, 781.0, 780.0, 779.0, 778.0, 777.0, 776.0, 775.0, 774.0, 773.0, 772.0, 772.0, 771.0,
        770.0, 769.0, 768.0, 767.0, 767.0, 767.0,
    793.0, 792.0, 791.0, 790.0, 789.0, 788.0, 787.0, 786.0, 786.0, 785.0, 784.0, 783.0, 782.0,
        781.0, 780.0, 779.0, 778.0, 777.0, 776.0, 775.0, 774.0, 773.0, 772.0, 771.0, 771.0, 770.0,
        769.0, 768.0, 767.0, 766.0, 766.0, 766.0,
    793.0, 792.0, 791.0, 790.0, 789.0, 788.0, 787.0, 786.0, 786.0, 785.0, 784.0, 783.0, 782.0,
        781.0, 780.0, 779.0, 778.0, 777.0, 776.0, 775.0, 774.0, 773.0, 772.0, 771.0, 771.0, 770.0,
        769.0, 768.0, 767.0, 766.0, 765.0, 765.0,
  };

  private static final int MAX = 32;
  private Building building = null;

  private static Building createMockBuilding(
      final int id,
      final int timeSpotted,
      final UnitType unitType,
      final TilePosition tilePosition) {
    final Position position =
        tilePosition
            .toPosition()
            .add(
                new TilePosition(unitType.tileWidth() / 2, unitType.tileHeight() / 2).toPosition());
    return new BuildingMock(id, unitType, timeSpotted, tilePosition, position);
  }

  @BeforeClass
  public static void beforeClass() {
    try {
      BWDataProvider.injectValues();
    } catch (Exception e) {
      throw new RuntimeException("Something went wrong injecting BW values into this test.", e);
    }
  }

  @Before
  public void before() {
    building = createMockBuilding(0, 0, UnitType.Protoss_Nexus, new TilePosition(20, 20));
  }

  @Test
  public void TEST_getLastKnownDistance() {
    Pair<UnitMock, UnitMock> units = getTestUnits();
    UnitMock unit1 = units.getFirst();
    UnitMock unit2 = units.getSecond();

    for (int y = 0; y < MAX; ++y) {
      for (int x = 0; x < MAX; ++x) {
        final TilePosition tpos = new TilePosition(x, y);
        final Position pos = tpos.toPosition();
        unit1.x = x;
        unit1.y = y;
        unit2.x = x;
        unit2.y = y;

        final int index = MAX * y + x;

        Assert.assertEquals(
            LAST_KNOWN_DISTANCE_SAMPLES_BY_TILEPOSITION[index],
            building.getLastKnownDistance(tpos));
        Assert.assertEquals(
            LAST_KNOWN_DISTANCE_SAMPLES_BY_POSITION[index], building.getLastKnownDistance(pos), 0);
        Assert.assertEquals(
            LAST_KNOWN_DISTANCE_SAMPLES_BY_UNIT1[index], building.getLastKnownDistance(unit1), 0);
        Assert.assertEquals(
            LAST_KNOWN_DISTANCE_SAMPLES_BY_UNIT2[index], building.getLastKnownDistance(unit2), 0);
      }
    }
  }

  @Ignore
  @Test
  public void generateValuesForSamplesArray() {
    final StringBuilder positionValuesString = new StringBuilder();
    final StringBuilder tilePositionValuesString = new StringBuilder();
    final StringBuilder unitPositionValuesString1 = new StringBuilder();
    final StringBuilder unitPositionValuesString2 = new StringBuilder();
    Pair<UnitMock, UnitMock> units = getTestUnits();
    UnitMock unit1 = units.getFirst();
    UnitMock unit2 = units.getSecond();

    for (int y = 0; y < MAX; ++y) {
      for (int x = 0; x < MAX; ++x) {
        final TilePosition tpos = new TilePosition(x, y);
        final Position pos = tpos.toPosition();
        unit1.x = x;
        unit1.y = y;
        unit2.x = x;
        unit2.y = y;
        tilePositionValuesString.append(building.getLastKnownDistance(tpos)).append(", ");
        positionValuesString.append(building.getLastKnownDistance(pos)).append(", ");
        unitPositionValuesString1.append(building.getLastKnownDistance(unit1)).append(", ");
        unitPositionValuesString2.append(building.getLastKnownDistance(unit2)).append(", ");
      }
      tilePositionValuesString.append(System.lineSeparator());
      positionValuesString.append(System.lineSeparator());
      unitPositionValuesString1.append(System.lineSeparator());
      unitPositionValuesString2.append(System.lineSeparator());
    }

    System.out.println("By TilePosition: ");
    System.out.print(tilePositionValuesString.toString());
    System.out.println();
    System.out.println("By Position: ");
    System.out.print(positionValuesString.toString());
    System.out.println();
    System.out.println("By Unit1: ");
    System.out.print(unitPositionValuesString1.toString());
    System.out.println();
    System.out.println("By Unit2: ");
    System.out.print(unitPositionValuesString2.toString());
  }

  private static Pair<UnitMock, UnitMock> getTestUnits() {
    return new Pair<>(
        new UnitMock(0, UnitType.Terran_Wraith), new UnitMock(1, UnitType.Terran_Science_Vessel));
  }
}
