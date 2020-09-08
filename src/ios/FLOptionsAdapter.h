#import <Foundation/Foundation.h>

@protocol FLRNCountryAdapter;
@protocol FLRNImageAdapter;
@protocol FLRNCardSchemesAdapter;

@interface FLOptionsAdapter : NSObject

@property (nonatomic, readonly) NSDictionary *constantsToExport;

-(instancetype)initWithCountryAdapter:(id<FLRNCountryAdapter>)countryAdapter
                         imageAdapter:(id<FLRNImageAdapter>)imageAdapter
                   cardSchemesAdapter:(id<FLRNCardSchemesAdapter>)cardSchemesAdapter;

-(void)setOptions: (NSDictionary *)options;

@end
