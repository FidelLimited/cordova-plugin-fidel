#import <Foundation/Foundation.h>

@protocol FLObjectToDictionaryAdapter <NSObject>

-(NSDictionary *)dictionaryFrom:(NSObject *)object;

@end
