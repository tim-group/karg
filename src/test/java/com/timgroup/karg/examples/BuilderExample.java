package com.timgroup.karg.examples;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.awt.Point;

import org.junit.Test;

import com.timgroup.karg.functions.KeywordFunction;
import com.timgroup.karg.keywords.Keyword;
import com.timgroup.karg.keywords.KeywordArgument;
import com.timgroup.karg.keywords.KeywordArguments;
import com.timgroup.karg.keywords.Keywords;

public class BuilderExample {
    
    public static class Line {
        public static interface LineMaker {
            Line to(Point end);
        }
        
        public static LineMaker from(final Point start) {
            return new LineMaker() {
                @Override public Line to(Point end) {
                    return new Line(start, end);
                }
            };
        }
        
        private final Point start;
        private final Point end;
        
        private Line(Point start, Point end) {
            this.start = start;
            this.end = end;
        }
        
        public Point getStart() {
            return start;
        }
        
        public Point getEnd() {
            return end;
        }
    }
    
    public static class LineBuilder implements KeywordFunction<Line> {
        
        public static final Keyword<Integer> START_X = Keywords.newKeyword();
        public static final Keyword<Integer> START_Y = Keywords.newKeyword();
        public static final Keyword<Integer> END_X = Keywords.newKeyword();
        public static final Keyword<Integer> END_Y = Keywords.newKeyword();
        
        public static Line a_line_with(KeywordArgument...args) {
            return new LineBuilder().apply(KeywordArguments.of(args));
        }
        
        @Override
        public Line apply(KeywordArguments args) {
            return Line.from(new Point(START_X.from(args), START_Y.from(args)))
                       .to(new Point(END_X.from(args), END_Y.from(args)));
        }
    }
    
    @Test public void
    canBuildANestedStructFromAFlatArgumentSet() {
        Line line = LineBuilder.a_line_with(LineBuilder.START_X.of(0),
                                            LineBuilder.START_Y.of(0),
                                            LineBuilder.END_X.of(100),
                                            LineBuilder.END_Y.of(200));
        
        assertThat(line.getStart().x, is(0));
        assertThat(line.getStart().y, is(0));
        assertThat(line.getEnd().x, is(100));
        assertThat(line.getEnd().y, is(200));
    }

}
