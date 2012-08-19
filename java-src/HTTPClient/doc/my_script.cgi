#!/usr/local/bin/perl

$len = read(STDIN, $text, $ENV{CONTENT_LENGTH});

print "Content-Type: text/plain\n";
print "Content-length: ".(30+$len)."\n\n";

binmode STDOUT;
print "Thank you for your message:\n\n$text\n";

