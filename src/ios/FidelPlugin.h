#import <Cordova/CDV.h>

@interface FidelPlugin : CDVPlugin

- (void)setup:(CDVInvokedUrlCommand*)command;
- (void)setOptions:(CDVInvokedUrlCommand*)command;
- (void)openForm:(CDVInvokedUrlCommand*)command;

@end