#!/usr/bin/make -F 
# http://www.gnu.org/software/make/manual/make.html

GRAPHVIZ=dot

%.svg: %.dot
	$(GRAPHVIZ) -Tsvg -o $@ $<
%.png: %.dot
	$(GRAPHVIZ)  -Tpng -o $@ $<

DOT=p2d.dot hampelmann.sg.dot
SVG=$(DOT:.dot=.svg)
PNG=$(DOT:.dot=.png)
IMAGES=$(SVG) $(PNG)
IMAGES=$(SVG)

all: $(IMAGES)

clean:
	rm $(IMAGES)